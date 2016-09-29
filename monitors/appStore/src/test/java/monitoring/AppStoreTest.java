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

public class AppStoreTest {
	
	String jsonWrongFormat = "wrongObject";
	String jsonITunesApple = "{\"AppStoreConfProf\": { \"toolName\": \"ITunesApple\", \"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"marketPlaces\", \"appId\": \"567630281\" } }";
	String jsonAppTweak = "{\"AppStoreConfProf\": { \"toolName\": \"AppTweak\", \"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"marketPlaces\", \"appId\": \"567630281\" } }";
	String jsonConfMissingTool = "{\"AppStoreConfProf\": {\"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"marketPlaces\", \"appId\": \"567630281\" } }";
	String jsonConfWrongTool = "{\"AppStoreConfProf\": { \"toolName\": \"WrongTool\", \"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"marketPlaces\", \"appId\": \"567630281\" } }";

	Client client = ClientBuilder.newBuilder()
            .register(JacksonJaxbJsonProvider.class)
            .build();
	
	WebTarget target = client
            .target("http://localhost:8080/appStore/configuration");

	@Test
	public void addConfigurationITunesApple() throws JSONException {

        Response response = target.queryParam("configurationJson",
        		UriComponent.encode(jsonITunesApple, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(null);
        
        JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
	    assertEquals("success", jsonResponse.getJSONObject("AppStoreConfProfResult").get("status"));
	    assertEquals(1, jsonResponse.getJSONObject("AppStoreConfProfResult").get("idConf"));
	    
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
	    assertEquals("success", jsonResponse.getJSONObject("AppStoreConfProfResult").get("status"));
	    assertEquals(1, jsonResponse.getJSONObject("AppStoreConfProfResult").get("idConf"));
	    
	}
	
	@Test
	public void missingToolNameError() throws JSONException {
	
	    Response response = target.queryParam("configurationJson", 
	    		UriComponent.encode(jsonConfMissingTool, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
	            .request(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .post(null);
	    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Missing tool name", jsonResponse.getJSONObject("AppStoreConfProfResult").get("message"));
	}
	
	@Test
	public void notExistingTool() throws JSONException {
	
	    Response response = target.queryParam("configurationJson", 
	    		UriComponent.encode(jsonConfWrongTool, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
	            .request(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .post(null);
	    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Not existing tool", jsonResponse.getJSONObject("AppStoreConfProfResult").get("message"));
	}
	
	@Test
	public void wrongJsonFormat() throws JSONException {
		
		Response response = target.queryParam("configurationJson", 
		    		UriComponent.encode(jsonWrongFormat, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
		            .request(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON)
		            .post(null);
		    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Not a valid JSON configuration object", jsonResponse.getJSONObject("AppStoreConfProfResult").get("message"));
		
	}
	
}
