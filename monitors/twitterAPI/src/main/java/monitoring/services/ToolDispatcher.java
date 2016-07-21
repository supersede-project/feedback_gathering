package monitoring.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringParams;

@Path("configuration")
@Singleton
public class ToolDispatcher {
	
	//Fake configuration id for testing purposes
	private int confId = 1;
	private final String packageRoute = "monitoring.tools.";
	
	//A data structure storing all monitoring tool instances identified by configuration ID
	private Map<Integer, ToolInterface> monitoringInstances = new HashMap<>();
	
	/**
	 * Adds a new configuration for the implicit monitor and creates a new monitoring instance
	 * @param jsonConf		the JSON monitor configuration
	 * @return				response
	 */
	@POST
	public String addConfiguration(@QueryParam("configurationJson") String jsonConf) {
				
		try {
			MonitoringParams params = parseJsonConfiguration(jsonConf);
			
			if (params.getToolName() == null) 
				return throwError(confId, "Missing tool name");
			
			Class monitor = Class.forName(packageRoute + params.getToolName());
			ToolInterface toolInstance = (ToolInterface) monitor.newInstance();
			toolInstance.addConfiguration(params, KafkaCommunication.initProducer(params.getKafkaEndpoint()));
			
			monitoringInstances.put(confId, toolInstance);
			
		} catch (JSONException e) {
			return throwError(confId, "Not a valid JSON configuration object");
		} catch (ClassNotFoundException e) {
			return throwError(confId, "Not existing tool");
		} catch (InstantiationException e) {
			return throwError(confId, "Monitor class must be concrete");
		} catch (IllegalAccessException e) {
			return throwError(confId, "Monitor class must have a constructor with no args");
		} catch (Exception e) {
			return throwError(confId, "The selected tool is not working properly");
		}
		
		return getResponse(confId);
	}
	
	/**
	 * Deletes the implicit monitoring and stops the monitoring
	 * @param id		the configuration id
	 * @return			response
	 */
	@DELETE
	@Path("{id}")
	public String deleteConfiguration(@PathParam("id") Integer id) {
				
		try {
			if (!monitoringInstances.containsKey(id))
				return throwError(id, "Not existing configuration with the specified ID");
			monitoringInstances.get(id).deleteConfiguration();
			monitoringInstances.remove(id);
		} catch (Exception e) {
			return throwError(id, "There was an unexpected error");
		}
		
		return getResponse(id);
		
	}
	
	private MonitoringParams parseJsonConfiguration(String json) throws Exception {
		
		MonitoringParams params = new MonitoringParams();
		
		JSONObject jsonParams = new JSONObject(json);
		jsonParams = jsonParams.getJSONObject("SocialNetworksMonitoringConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		params = new MonitoringParams();

		while( keys.hasNext() ) {
			
		    String key = (String)keys.next();
		    if (key.equals("timeSlot")) params.setTimeSlot(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("toolName")) params.setToolName(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("kafkaEndpoint")) params.setKafkaEndpoint(jsonParams.getString(key).replaceAll("\"", "").replace("http://", ""));
		    else if (key.equals("kafkaTopic")) params.setKafkaTopic(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("keywordExpression")) params.setKeywordExpression(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("accounts")) {
		    	JSONArray jsonAccounts = jsonParams.getJSONArray(key);
	    		List<String> accounts = new ArrayList<>();
	    		for (int i = 0; i < jsonAccounts.length(); ++i) {
	    			accounts.add(jsonAccounts.getString(i).replaceAll("\"", "").replaceAll("@", ""));
	    		}
	    		params.setAccounts(accounts);
		    }
		}
		
		return params;
		
	}

	public String throwError(int id, String error) {
		
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		
		try {
			resInfo.put("message", error);
			resInfo.put("idConf", String.valueOf(id));
			resInfo.put("status", "error");
			response.put("SocialNetworksMonitoringConfProfResult", resInfo);
			++confId;		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return response.toString();
		
	}
	
	public String getResponse(int id) {
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		
		try {
			resInfo.put("idConf", String.valueOf(id));
			resInfo.put("status", "success");
			response.put("SocialNetworksMonitoringConfProfResult", resInfo);
			++confId;		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return response.toString();
	}

}
