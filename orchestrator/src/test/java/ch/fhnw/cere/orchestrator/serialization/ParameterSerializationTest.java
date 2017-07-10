package ch.fhnw.cere.orchestrator.serialization;

import ch.fhnw.cere.orchestrator.models.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class ParameterSerializationTest {
    String parameterJson1;
    String parameterJson2;

    @Before
    public void setup() {
        parameterJson1 ="{" +
                "          \"key\": \"key\",\n" +
                "          \"value\": \"value\"\n" +
                "        }\n";

        parameterJson2 = "{\n" +
                "          \"key\": \"options\",\n" +
                "          \"parameters\": [\n" +
                "            {\n" +
                "              \"key\": \"BUG_CATEGORY\",\n" +
                "              \"value\": \"Bug\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"key\": \"FEATURE_REQUEST_CATEGORY\",\n" +
                "              \"value\": \"Feature Request\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"key\": \"GENERAL_CATEGORY\",\n" +
                "              \"value\": \"General Feedback\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }";
    }

    @Test
    public void testDeserializationOfParameter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Parameter parameter1 = mapper.readValue(parameterJson1, Parameter.class);

        assertEquals("key", parameter1.getKey());
        assertEquals("value", parameter1.getValue());
    }

    @Test
    public void testDeserializationOfParameterWithNestedParameters() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Parameter parameter1 = mapper.readValue(parameterJson2, Parameter.class);

        assertEquals("options", parameter1.getKey());
        assertEquals(null, parameter1.getValue());
        assertEquals(3, parameter1.getParameters().size());
        assertEquals("BUG_CATEGORY", parameter1.getParameters().get(0).getKey());
        assertEquals("Bug", parameter1.getParameters().get(0).getValue());
        assertEquals("FEATURE_REQUEST_CATEGORY", parameter1.getParameters().get(1).getKey());
        assertEquals("Feature Request", parameter1.getParameters().get(1).getValue());
        assertEquals("GENERAL_CATEGORY", parameter1.getParameters().get(2).getKey());
        assertEquals("General Feedback", parameter1.getParameters().get(2).getValue());
    }
}
