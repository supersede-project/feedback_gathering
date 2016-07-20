package monitoring.tools;

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

public class TwitterAPITest {
	
	String jsonWrongFormat = "wrongObject";
	String jsonConfOK = "{\"SocialNetworksMonitoringConfProf\": { \"toolName\": \"TwitterAPI\", \"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"tweeterMonitoring\", \"keywordExpression\": \"(tweet OR follow) AND (me)\", \"accounts\": [\"QuimMotger\"] } }";
	String jsonConfMissingTool = "{\"SocialNetworksMonitoringConfProf\": {\"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"tweeterMonitoring\", \"keywordExpression\": \"(tweet OR follow) AND (me)\", \"accounts\": [\"QuimMotger\"] } }";
	String jsonConfWrongTool = "{\"SocialNetworksMonitoringConfProf\": { \"toolName\": \"WrongTool\", \"timeSlot\": \"30\", \"kafkaEndpoint\": \"http://localhost:9092\", \"kafkaTopic\": \"tweeterMonitoring\", \"keywordExpression\": \"(tweet OR follow) AND (me)\", \"accounts\": [\"QuimMotger\"] } }";
	
	Client client = ClientBuilder.newBuilder()
            .register(JacksonJaxbJsonProvider.class)
            .build();
	
	WebTarget target = client
            .target("http://localhost:8080/twitterAPI/service/configuration");
	
	@Test
    public void addConfigurationSuccess() {
 
        Response response = target.queryParam("configurationJson", 
        		UriComponent.encode(jsonConfOK, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(null);
  
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

    }

	@Test
	public void addConfigurationOKResults() throws JSONException {

        Response response = target.queryParam("configurationJson",
        		UriComponent.encode(jsonConfOK, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(null);
        
        JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("success", jsonResponse.getJSONObject("SocialNetworksMonitoringConfProfResult").get("status"));
	    assertEquals(1, jsonResponse.getJSONObject("SocialNetworksMonitoringConfProfResult").get("idConf"));
	    
	}
	
	@Test
	public void missingToolNameError() throws JSONException {
	
	    Response response = target.queryParam("configurationJson", 
	    		UriComponent.encode(jsonConfMissingTool, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
	            .request(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .post(null);
	    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Missing tool name", jsonResponse.getJSONObject("SocialNetworksMonitoringConfProfResult").get("message"));
	}
	
	@Test
	public void notExistingTool() throws JSONException {
	
	    Response response = target.queryParam("configurationJson", 
	    		UriComponent.encode(jsonConfWrongTool, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
	            .request(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .post(null);
	    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Not existing tool", jsonResponse.getJSONObject("SocialNetworksMonitoringConfProfResult").get("message"));
	}
	
	@Test
	public void wrongJsonFormat() throws JSONException {
		
		Response response = target.queryParam("configurationJson", 
		    		UriComponent.encode(jsonWrongFormat, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED))
		            .request(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON)
		            .post(null);
		    
	    JSONObject jsonResponse = new JSONObject(response.readEntity(String.class));
	        
	    assertEquals("Not a valid JSON configuration object", jsonResponse.getJSONObject("SocialNetworksMonitoringConfProfResult").get("message"));
		
	}
	
	
}
