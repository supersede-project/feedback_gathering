package ch.fhnw.cere.orchestrator.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


public class ParameterValueSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if(value.equals("true")) {
            gen.writeObject(true);
        } else if(value.equals("false")) {
            gen.writeObject(false);
        } else {
            gen.writeObject(value);
        }
    }
}
