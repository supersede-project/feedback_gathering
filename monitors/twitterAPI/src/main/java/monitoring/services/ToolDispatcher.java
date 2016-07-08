package monitoring.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringParams;

@Path("service")
public class ToolDispatcher {
	
	//The id of the response associated to a addConfiguration call
	private int responseId = 1;
	private String packageRoute = "monitoring.tools.";
	
	@POST
	@Path("/configuration")
	public String addConfiguration(@QueryParam("configurationJson") String jsonConf) {
				
		try {
			MonitoringParams params = parseJsonConfiguration(jsonConf);
			if (params.getToolName() == null) 
				return throwError("Missing tool name");
			Class monitor = Class.forName(packageRoute + params.getToolName());
			ToolInterface serviceInstance = (ToolInterface) monitor.newInstance();
			serviceInstance.addConfiguration(params, KafkaCommunication.initProducer(params.getKafkaEndpoint()));
			
		} catch (JSONException e) {
			return throwError("Not a valid JSON configuration object");
		} catch (ClassNotFoundException e) {
			return throwError("Not existing tool");
		} catch (InstantiationException e) {
			return throwError("Monitor class must be concrete");
		} catch (IllegalAccessException e) {
			return throwError("Monitor class must have a constructor with no args");
		} catch (Exception e) {
			return throwError("The selected tool is not working properly");
		}
		
		return getResponse();
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

	public String throwError(String error) {
		
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		
		try {
			resInfo.put("message", error);
			resInfo.put("idConf", responseId);
			resInfo.put("status", "error");
			response.put("SocialNetworksMonitoringConfProfResult", resInfo);
			++responseId;		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return response.toString();
		
	}
	
	public String getResponse() {
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		
		try {
			resInfo.put("idConf", responseId);
			resInfo.put("status", "success");
			response.put("SocialNetworksMonitoringConfProfResult", resInfo);
			++responseId;		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return response.toString();
	}

}
