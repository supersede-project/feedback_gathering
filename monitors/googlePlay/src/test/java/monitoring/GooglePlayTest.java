package monitoring;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.glassfish.jersey.uri.UriComponent;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class GooglePlayTest {
	
	String jsonWrongFormat = "wrongObject";
	String jsonGooglePlayAPI = "{\"GooglePlayConfProf\": { \"toolName\": \"GooglePlayAPI\", \"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"marketPlaces\", \"packageName\": \"dev.blunch.blunch\" } }";
	String jsonAppTweak = "{\"GooglePlayConfProf\": { \"toolName\": \"AppTweak\", \"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"marketPlaces\", \"packageName\": \"dev.blunch.blunch\" } }";
	String jsonConfMissingTool = "{\"GooglePlayConfProf\": {\"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"marketPlaces\", \"packageName\": \"dev.blunch.blunch\" } }";
	String jsonConfWrongTool = "{\"GooglePlayConfProf\": { \"toolName\": \"WrongTool\", \"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"marketPlaces\", \"packageName\": \"dev.blunch.blunch\" } }";

	Client client = ClientBuilder.newBuilder()
            .register(JacksonJaxbJsonProvider.class)
            .build();
	
	WebTarget target = client
            .target("http://localhost:8080/googlePlay/configuration");

	/*@Test
	public void addConfigurationGooglePlayAPI() throws JSONException {

        Response response = target.queryParam("configurationJson",
        		UriComponent.encode(jsonGooglePlayAPI, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(null);
        
        JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
	    assertEquals("success", jsonResponse.getJSONObject("GooglePlayConfProfResult").get("status"));
	    assertEquals(1, jsonResponse.getJSONObject("GooglePlayConfProfResult").get("idConf"));
	    
	}
	
	@Test
	public void addConfigurationAppTweak() throws JSONException {

        Response response = target.queryParam("configurationJson",
        		UriComponent.encode(jsonAppTweak, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(null);
        
        JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
	    assertEquals("success", jsonResponse.getJSONObject("GooglePlayConfProfResult").get("status"));
	    assertEquals(1, jsonResponse.getJSONObject("GooglePlayConfProfResult").get("idConf"));
	    
	}
	
	@Test
	public void missingToolNameError() throws JSONException {
	
	    Response response = target.queryParam("configurationJson", 
	    		UriComponent.encode(jsonConfMissingTool, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
	            .request(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .post(null);
	    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Missing tool name", jsonResponse.getJSONObject("GooglePlayConfProfResult").get("message"));
	}
	
	@Test
	public void notExistingTool() throws JSONException {
	
	    Response response = target.queryParam("configurationJson", 
	    		UriComponent.encode(jsonConfWrongTool, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
	            .request(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .post(null);
	    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Not existing tool", jsonResponse.getJSONObject("GooglePlayConfProfResult").get("message"));
	}
	
	@Test
	public void wrongJsonFormat() throws JSONException {
		
		Response response = target.queryParam("configurationJson", 
		    		UriComponent.encode(jsonWrongFormat, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
		            .request(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON)
		            .post(null);
		    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Not a valid JSON configuration object", jsonResponse.getJSONObject("GooglePlayConfProfResult").get("message"));
		
	}*/
	
}
