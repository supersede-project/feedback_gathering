package ch.fhnw.cere.orchestrator.serialization;


import ch.fhnw.cere.orchestrator.models.Parameter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ParameterSerializationTest {
    private String parameterJson1;
    private String parameterJson2;
    private String parameterJson3;
    private String parameterJson4;
    private String parameterJson5;

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

        parameterJson3 = "{\n" +
                "          \"key\": \"options\",\n" +
                "          \"value\": [\n" +
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

        parameterJson4 = "[\n" +
                "              {\n" +
                "                \"key\": \"mandatoryReminder\",\n" +
                "                \"value\": \"Please select at least one category\",\n" +
                "                \"language\": \"en\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"key\": \"options\",\n" +
                "                \"value\": [\n" +
                "                  {\n" +
                "                    \"key\": \"SLOW_CONNECTION\",\n" +
                "                    \"value\": \"Slow connection\",\n" +
                "                    \"language\": \"en\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"key\": \"VIDEO_IS_FREEZING\",\n" +
                "                    \"value\": \"Video is freezing\",\n" +
                "                    \"language\": \"en\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"key\": \"BUG_CATEGORY\",\n" +
                "                    \"value\": \"Bug\",\n" +
                "                    \"language\": \"en\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"key\": \"FEATURE_REQUEST_CATEGORY\",\n" +
                "                    \"value\": \"Feature request\",\n" +
                "                    \"defaultValue\": \"\",\n" +
                "                    \"editableByUser\": false,\n" +
                "                    \"language\": \"en\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"key\": \"GENERAL_CATEGORY\",\n" +
                "                    \"value\": \"General feedback\",\n" +
                "                    \"language\": \"en\"\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"language\": \"en\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"key\": \"ownAllowed\",\n" +
                "                \"value\": 0.0,\n" +
                "                \"language\": \"en\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"key\": \"multiple\",\n" +
                "                \"value\": 0.0,\n" +
                "                \"language\": \"en\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"key\": \"mandatory\",\n" +
                "                \"value\": 1.0,\n" +
                "                \"language\": \"en\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"key\": \"title\",\n" +
                "                \"value\": \"Please choose one of the following categories\",\n" +
                "                \"language\": \"en\"\n" +
                "              }\n" +
                "            ]";
        parameterJson5 =  "{\n" +
                "                \"key\": \"options\",\n" +
                "                \"value\": [\n" +
                "                  {\n" +
                "                    \"key\": \"SLOW_CONNECTION\",\n" +
                "                    \"value\": \"Slow connection\",\n" +
                "                    \"language\": \"en\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"key\": \"VIDEO_IS_FREEZING\",\n" +
                "                    \"value\": \"Video is freezing\",\n" +
                "                    \"language\": \"en\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"key\": \"BUG_CATEGORY\",\n" +
                "                    \"value\": \"Bug\",\n" +
                "                    \"language\": \"en\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"key\": \"FEATURE_REQUEST_CATEGORY\",\n" +
                "                    \"value\": \"Feature request\",\n" +
                "                    \"defaultValue\": \"\",\n" +
                "                    \"editableByUser\": false,\n" +
                "                    \"language\": \"en\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"key\": \"GENERAL_CATEGORY\",\n" +
                "                    \"value\": \"General feedback\",\n" +
                "                    \"language\": \"en\"\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"language\": \"en\"\n" +
                "              },\n";
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

    @Test
    public void testDeserializationOfParameterWithNestedParameters2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Parameter parameter = mapper.readValue(parameterJson5, Parameter.class);

        assertEquals("options", parameter.getKey());
        assertEquals(null, parameter.getValue());
        assertEquals(5, parameter.getParameters().size());
    }

    @Test
    public void testDeserializationOfParameterWithNestedParametersInList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Parameter> parameters = mapper.readValue(parameterJson4, new TypeReference<List<Parameter>>() {});

        assertEquals(6, parameters.size());
        assertEquals("options", parameters.get(1).getKey());
        assertEquals(5, parameters.get(1).getParameters().size());
    }

    @Test
    public void testDeserializationOfParameterWithNestedParametersAtValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Parameter parameter1 = mapper.readValue(parameterJson3, Parameter.class);

        assertEquals("options", parameter1.getKey());
        assertEquals(null, parameter1.getValue());
    }

    @Test
    public void testSerialization() throws JsonProcessingException, JSONException {
        Parameter parameter = new Parameter("title", "Title", new Date(), new Date(), "en", null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        String parameterJson = mapper.writeValueAsString(parameter);
        JSONObject data = new JSONObject(parameterJson);

        assertEquals("title", data.getString("key"));
        assertEquals("Title", data.getString("value"));
        assertEquals("en", data.getString("language"));


        Parameter parameter2 = new Parameter("title", null, new Date(), new Date(), "en", null, null, null);

        String parameterJson2 = mapper.writeValueAsString(parameter2);
        JSONObject data2 = new JSONObject(parameterJson2);

        assertEquals("title", data2.getString("key"));
        assertEquals(JSONObject.NULL, data2.get("value"));
        assertEquals("en", data2.getString("language"));


        Parameter parameter3 = new Parameter("boolean", "true", new Date(), new Date(), "en", null, null, null);

        String parameterJson3 = mapper.writeValueAsString(parameter3);
        JSONObject data3 = new JSONObject(parameterJson3);

        assertEquals("boolean", data3.getString("key"));
        assertEquals(true, data3.get("value"));
        assertEquals("en", data3.getString("language"));
    }

    @Test
    public void testSerializationOfParameterWithNestedParameters() throws JsonProcessingException, JSONException {
        Parameter parentParameter1 = new Parameter("options", null, new Date(), new Date(), "en", null, null, null);
        Parameter childParameter1 = new Parameter("CAT_1", "Cat 1 EN", new Date(), new Date(), "en", parentParameter1, null, null);
        Parameter childParameter2 = new Parameter("CAT_2", "Cat 2 EN", new Date(), new Date(), "en", parentParameter1, null, null);

        Parameter childParameter3 = new Parameter("CHILD_LEVEL_1", "Child level 1", new Date(), new Date(), "en", parentParameter1, null, null);
        Parameter childOfChildParameter1 = new Parameter("CHILD_LEVEL_2", "Child level 2", new Date(), new Date(), "en", childParameter3, null, null);
        List<Parameter> childParametersLevel2 = new ArrayList<Parameter>() {
            {
                add(childOfChildParameter1);
            }
        };
        childParameter3.setParameters(childParametersLevel2);

        List<Parameter> childParameters = new ArrayList<Parameter>() {
            {
                add(childParameter1);
                add(childParameter2);
                add(childParameter3);
            }
        };
        parentParameter1.setParameters(childParameters);
        ObjectMapper mapper = new ObjectMapper();
        String parameterJson = mapper.writeValueAsString(parentParameter1);

        JSONObject data = new JSONObject(parameterJson);
        assertEquals("CAT_1", data.getJSONArray("value").getJSONObject(0).getString("key"));
        assertEquals("Cat 1 EN", data.getJSONArray("value").getJSONObject(0).getString("value"));
        assertEquals("CAT_2", data.getJSONArray("value").getJSONObject(1).getString("key"));
        assertEquals("Cat 2 EN", data.getJSONArray("value").getJSONObject(1).getString("value"));
        assertEquals("CHILD_LEVEL_1", data.getJSONArray("value").getJSONObject(2).getString("key"));
        assertEquals("CHILD_LEVEL_2", data.getJSONArray("value").getJSONObject(2).getJSONArray("value").getJSONObject(0).getString("key"));
        assertEquals("Child level 2", data.getJSONArray("value").getJSONObject(2).getJSONArray("value").getJSONObject(0).getString("value"));
    }
}
