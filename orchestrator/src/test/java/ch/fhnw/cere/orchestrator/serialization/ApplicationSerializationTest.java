package ch.fhnw.cere.orchestrator.serialization;

import ch.fhnw.cere.orchestrator.controllers.helpers.ApplicationTreeBuilder;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.models.MechanismType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ApplicationSerializationTest {
    String applicationJson1;
    String applicationJson2;

    @Before
    public void setup() {
        applicationJson1 = "{\"id\":0,\"name\":\"Test App 4\",\"state\":1,\"createdAt\":1495802498709,\"updatedAt\":1495802498709}\n";
        applicationJson2 = "{\"id\":0,\"name\":\"Test App 4\",\"state\":1,\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"configurations\":[{\"id\":0,\"name\":\"Push configuration 1 of Test App 4\",\"type\":\"PUSH\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"mechanisms\":[{\"id\":0,\"type\":\"TEXT_TYPE\",\"createdAt\":null,\"updatedAt\":null,\"active\":true,\"order\":1,\"parameters\":[{\"id\":0,\"key\":\"title\",\"value\":\"Title EN\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"language\":\"en\",\"parameters\":null},{\"id\":0,\"key\":\"title\",\"value\":\"Titel DE\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"language\":\"de\",\"parameters\":null},{\"id\":0,\"key\":\"font-size\",\"value\":\"10\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"language\":\"en\",\"parameters\":null},{\"id\":0,\"key\":\"options\",\"value\":null,\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"language\":\"en\",\"parameters\":[{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 EN\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"language\":\"en\",\"parameters\":null},{\"id\":0,\"key\":\"CAT_2\",\"value\":\"Cat 2 EN\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"language\":\"en\",\"parameters\":null},{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 DE\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"language\":\"de\",\"parameters\":null},{\"id\":0,\"key\":\"CAT_3\",\"value\":\"Cat 3 FR\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"language\":\"fr\",\"parameters\":null}]}]}]},{\"id\":0,\"name\":\"Pull configuration 1 of Test App 4\",\"type\":\"PULL\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"mechanisms\":[{\"id\":0,\"type\":\"TEXT_TYPE\",\"createdAt\":null,\"updatedAt\":null,\"active\":true,\"order\":1,\"parameters\":[]}]}]}\n";
    }

    @Test
    public void testDeserializationOfApplication() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Application application1 = mapper.readValue(applicationJson1, Application.class);

        assertEquals(application1.getName(), "Test App 4");
        assertEquals(application1.getState(), 1);

        Application application2 = mapper.readValue(applicationJson2, Application.class);
        assertEquals(application2.getName(), "Test App 4");
        assertEquals(application2.getState(), 1);
        assertEquals(application2.getConfigurations().size(), 2);
        assertEquals(application2.getConfigurations().get(0).getMechanisms().size(), 1);
    }

    @Test
    public void testSerialization() throws IOException {
        ApplicationTreeBuilder applicationTreeBuilder = new ApplicationTreeBuilder();
        Application application = applicationTreeBuilder.buildApplicationTree("Test App 4");

        assertEquals(2, application.getConfigurations().size());

        System.err.println(application);

        // ??? When reversing the list, the serialization and deserialization works...
        application.setConfigurations(Lists.reverse(application.getConfigurations()));

        System.err.println(application);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(application);

        Application application1 = mapper.readValue(jsonString, Application.class);
        assertEquals(2, application1.getConfigurations().size());
    }

    @Test
    public void testConfigurationSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String configurationJson = "{\"id\":0,\"name\":\"Push configuration 1 of Test App 4\",\"type\":\"PUSH\",\"createdAt\":1510220152806,\"updatedAt\":1510220152806,\"generalConfiguration\":{\"id\":0,\"name\":\"General configuration for push configuration\",\"createdAt\":1510220152813,\"updatedAt\":1510220152813,\"parameters\":[{\"id\":0,\"key\":\"reviewActive\",\"value\":false,\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.813+0100\",\"updatedAt\":\"2017-11-09T10:35:52.813+0100\"}]},\"pullDefault\":false,\"pushDefault\":true,\"mechanisms\":[{\"id\":0,\"type\":\"TEXT_TYPE\",\"createdAt\":null,\"updatedAt\":null,\"active\":true,\"order\":1,\"parameters\":[{\"id\":0,\"key\":\"title\",\"value\":\"Title EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"title\",\"value\":\"Titel DE\",\"parameters\":[],\"language\":\"de\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"font-size\",\"value\":\"10\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"options\",\"value\":[{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_2\",\"value\":\"Cat 2 EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 DE\",\"parameters\":[],\"language\":\"de\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_3\",\"value\":\"Cat 3 FR\",\"parameters\":[],\"language\":\"fr\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"}],\"parameters\":[{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_2\",\"value\":\"Cat 2 EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 DE\",\"parameters\":[],\"language\":\"de\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_3\",\"value\":\"Cat 3 FR\",\"parameters\":[],\"language\":\"fr\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"}],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"}]}],\"userGroups\":[]},{\"id\":0,\"name\":\"Pull configuration 1 of Test App 4\",\"type\":\"PULL\",\"createdAt\":1510220152806,\"updatedAt\":1510220152806,\"generalConfiguration\":null,\"pullDefault\":true,\"pushDefault\":false,\"mechanisms\":[{\"id\":0,\"type\":\"TEXT_TYPE\",\"createdAt\":null,\"updatedAt\":null,\"active\":true,\"order\":1,\"parameters\":[]}],\"userGroups\":[]}";
        Configuration configuration = mapper.readValue(configurationJson, Configuration.class);
        assertEquals("Push configuration 1 of Test App 4", configuration.getName());

        String configurationJson2 = "{\"id\":0,\"name\":\"Push configuration 1 of Test App 4\",\"type\":\"PUSH\",\"createdAt\":1510220152806,\"updatedAt\":1510220152806,\"pullDefault\":false,\"pushDefault\":true,\"mechanisms\":[{\"id\":0,\"type\":\"TEXT_TYPE\",\"createdAt\":null,\"updatedAt\":null,\"active\":true,\"order\":1,\"parameters\":[{\"id\":0,\"key\":\"title\",\"value\":\"Title EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"title\",\"value\":\"Titel DE\",\"parameters\":[],\"language\":\"de\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"font-size\",\"value\":\"10\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"options\",\"value\":[{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_2\",\"value\":\"Cat 2 EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 DE\",\"parameters\":[],\"language\":\"de\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_3\",\"value\":\"Cat 3 FR\",\"parameters\":[],\"language\":\"fr\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"}],\"parameters\":[{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_2\",\"value\":\"Cat 2 EN\",\"parameters\":[],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_1\",\"value\":\"Cat 1 DE\",\"parameters\":[],\"language\":\"de\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"},{\"id\":0,\"key\":\"CAT_3\",\"value\":\"Cat 3 FR\",\"parameters\":[],\"language\":\"fr\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"}],\"language\":\"en\",\"createdAt\":\"2017-11-09T10:35:52.809+0100\",\"updatedAt\":\"2017-11-09T10:35:52.809+0100\"}]}],\"userGroups\":[]},{\"id\":0,\"name\":\"Pull configuration 1 of Test App 4\",\"type\":\"PULL\",\"createdAt\":1510220152806,\"updatedAt\":1510220152806,\"generalConfiguration\":null,\"pullDefault\":true,\"pushDefault\":false,\"mechanisms\":[{\"id\":0,\"type\":\"TEXT_TYPE\",\"createdAt\":null,\"updatedAt\":null,\"active\":true,\"order\":1,\"parameters\":[]}],\"userGroups\":[]}";
        Configuration configuration2 = mapper.readValue(configurationJson2, Configuration.class);
        assertEquals("Push configuration 1 of Test App 4", configuration2.getName());

        String configurationJson3 = "{\"id\":0,\"name\":\"Pull configuration 1 of Test App 4\",\"type\":\"PULL\",\"createdAt\":1495802498709,\"updatedAt\":1495802498709,\"mechanisms\":[{\"id\":0,\"type\":\"TEXT_TYPE\",\"createdAt\":null,\"updatedAt\":null,\"active\":true,\"order\":1,\"parameters\":[]}]}";
        Configuration configuration3 = mapper.readValue(configurationJson3, Configuration.class);
        assertEquals("Pull configuration 1 of Test App 4", configuration3.getName());

        ApplicationTreeBuilder applicationTreeBuilder = new ApplicationTreeBuilder();
        Application application = applicationTreeBuilder.buildApplicationTree("Test App 4");
        String jsonStringPush = mapper.writeValueAsString(application.getConfigurations().get(0));
        String jsonStringPull = mapper.writeValueAsString(application.getConfigurations().get(1));

        Configuration configuration4 = mapper.readValue(jsonStringPush, Configuration.class);
        Configuration configuration5 = mapper.readValue(jsonStringPull, Configuration.class);

        assertEquals("Push configuration 1 of Test App 4", configuration4.getName());
        assertEquals("Pull configuration 1 of Test App 4", configuration5.getName());

        List<Configuration> configurations = new ArrayList<Configuration>(){{
           add(configuration4);
           add(configuration5);
        }};
        String configurationsJson = mapper.writeValueAsString(configurations);
        List<Configuration> deserializedConfigurations = mapper.readValue(configurationsJson, new TypeReference<List<Configuration>>(){});
        assertEquals(2, deserializedConfigurations.size());
    }
}
