package monitoring.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import monitoring.params.MonitoringParams;

public abstract class ServiceWrapper {
	
	public abstract String addConfiguration(String jsonConf);
	
	public JSONObject getResponse(int id, String status) {
		
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		
		try {
			
			resInfo.put("idConf", id);
			resInfo.put("status", status);
			response.put("SocialNetworksMonitoringConfProfResult", resInfo);
			
			return response;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	public MonitoringParams parseConfigurationParams(String json) throws JSONException {
		
		JSONObject jsonParams = new JSONObject(json);
		jsonParams = jsonParams.getJSONObject("SocialNetworksMonitoringConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		MonitoringParams params = new MonitoringParams();

		while( keys.hasNext() ) {
			
		    String key = (String)keys.next();
		    if (key.equals("timeSlot")) params.setTimeSlot(jsonParams.getString(key));
		    else if (key.equals("kafkaEndpoint")) params.setKafkaEndpoint(jsonParams.getString(key));
		    else if (key.equals("kafkaTopic")) params.setKafkaTopic(jsonParams.getString(key));
		    else if (key.equals("keywordExpression")) params.setKeywordExpression(jsonParams.getString(key));
		    else if (key.equals("accounts")) {
		    	JSONArray jsonAccounts = jsonParams.getJSONArray(key);
	    		List<String> accounts = new ArrayList<>();
	    		for (int i = 0; i < jsonAccounts.length(); ++i) {
	    			accounts.add(jsonAccounts.getString(i));
	    		}
	    		params.setAccounts(accounts);
		    }
		}
		
		return params;
		
	}

}
