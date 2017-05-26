package ch.fhnw.cere.orchestrator.serialization;

import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.models.ConfigurationMechanism;
import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.models.TriggerType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ConfigurationDeserializer extends StdDeserializer<Configuration> {

    public ConfigurationDeserializer() {
        this(null);
    }

    public ConfigurationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Configuration deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        String configurationString = jsonParser.getText();
        System.err.println("deserializing configuration:" + configurationString);

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String name = node.get("name").asText();
        String triggerType = node.get("type").asText();
        TriggerType type = triggerType.equals("PUSH") ? TriggerType.PUSH : TriggerType.PULL;


        ObjectMapper mapper = new ObjectMapper();
        List<Mechanism> mechanisms = mapper.readValue(node.get("mechanisms").asText(), new TypeReference<List<Mechanism>>(){});

        Configuration configuration = new Configuration(name, type, null, null);

        List<ConfigurationMechanism> configurationMechanisms = new ArrayList<>();
        for(Mechanism mechanism : mechanisms) {
            ConfigurationMechanism configurationMechanism = new ConfigurationMechanism(configuration, mechanism, mechanism.isActive(), mechanism.getOrder(), new Date(), new Date());
            configurationMechanisms.add(configurationMechanism);
        }
        configuration.setConfigurationMechanisms(configurationMechanisms);

        return configuration;
    }
}
