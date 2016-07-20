package monitoring.services;

import java.util.Iterator;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.json.JSONException;
import org.json.JSONObject;

import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringParams;

@Path("configuration")
public class ToolDispatcher {

	//The id of the response associated to a addConfiguration call
	private int responseId = 1;
	private final String toolPackageRoute = "monitoring.tools.";
	
	@POST
	public String addConfiguration(@QueryParam("configurationJson") String jsonConf) {
		
		try {
			MonitoringParams params = parseJsonConfiguration(jsonConf);
			if (params.getToolName() == null) 
				return throwError("Missing tool name");
			
			Class monitor = Class.forName(toolPackageRoute + params.getToolName());
			ToolInterface toolInstance = (ToolInterface) monitor.newInstance();
			toolInstance.addConfiguration(params, KafkaCommunication.initProducer(params.getKafkaEndpoint()));
			
			return getResponse();
			
		} catch (JSONException e) {
			return throwError("Not a valid JSON configuration object");
		} catch (InstantiationException e) {
			return throwError("Monitor class must be concrete");
		} catch (IllegalAccessException e) {
			return throwError("Monitor class must have a constructor with no args");
		} catch (ClassNotFoundException e) {
			return throwError("Not existing tool");
		} catch (Exception e) {
			return throwError(e.getMessage());
		}
		
	}
	
	private MonitoringParams parseJsonConfiguration(String json) {
		
		MonitoringParams params = new MonitoringParams();
		
		JSONObject jsonParams = new JSONObject(json);
		jsonParams = jsonParams.getJSONObject("GooglePlayConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		params = new MonitoringParams();

		while( keys.hasNext() ) {
			
		    String key = (String)keys.next();
		    if (key.equals("toolName")) params.setToolName(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("timeSlot")) params.setTimeSlot(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("kafkaEndpoint")) params.setKafkaEndpoint(jsonParams.getString(key).replaceAll("\"", "").replace("http://", ""));
		    else if (key.equals("kafkaTopic")) params.setKafkaTopic(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("packageName")) params.setPackageName(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("toolParams")) {
		    	
		    	Iterator<?> toolKeys = jsonParams.getJSONObject(key).keys();
		    	while (toolKeys.hasNext()) {
		    		
		    		String toolKey = (String)keys.next();
		    		if (toolKey.equals("country")) params.setCountry(jsonParams.getJSONObject(key).getString(toolKey).replaceAll("\"", ""));
		    		else if (toolKey.equals("language")) params.setLanguage(jsonParams.getJSONObject(key).getString(toolKey).replaceAll("\"", ""));
		    	}
		    	
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
			response.put("GooglePlayConfProfResult", resInfo);
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
			response.put("GooglePlayConfProfResult", resInfo);
			++responseId;		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return response.toString();
	}
	
	
}
