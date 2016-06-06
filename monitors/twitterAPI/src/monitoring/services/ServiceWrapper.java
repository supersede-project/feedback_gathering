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

import monitoring.params.MonitoringData;
import monitoring.params.MonitoringParams;

public abstract class ServiceWrapper {
	
	private int responseDataId = 1;
	private int responseId = 1;
	
	public abstract String addConfiguration(String jsonConf);
	
	public JSONObject getResponse(String status) {
		
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		
		try {
			
			resInfo.put("idConf", responseId);
			resInfo.put("status", status);
			response.put("SocialNetworksMonitoringConfProfResult", resInfo);
			
			++responseId;
			
			return response;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	public void getData(String timeStamp, List<MonitoringData> data) {
		
		try {
			JSONArray items = new JSONArray();
			for (MonitoringData item : data) {
				JSONObject jsonItem = new JSONObject();
				
				jsonItem.put("idItem", item.getId());
				jsonItem.put("timeStamp", item.getTimeStamp());
				jsonItem.put("message", item.getMessage());
				jsonItem.put("author", item.getAuthor());
				jsonItem.put("link", item.getLink());
				
				items.put(jsonItem);
			}
			
			JSONObject mainInfo = new JSONObject();
			
			mainInfo.put("idOutput", responseDataId);
			mainInfo.put("searchTimeStamp", timeStamp);
			mainInfo.put("numDataItems", data.size());
			mainInfo.put("DataItems", items);
			
			++responseDataId;
			
			JSONObject jsonData = new JSONObject();
			
			jsonData.put("socialNetworksMonitoredData", mainInfo);
			
			sendToKafka(jsonData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendToKafka(JSONObject data) {
		
		System.out.println(data);
		
	}

	public MonitoringParams parseConfigurationParams(String json) throws JSONException {
		
		JSONObject jsonParams = new JSONObject(json);
		jsonParams = jsonParams.getJSONObject("SocialNetworksMonitoringConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		MonitoringParams params = new MonitoringParams();

		while( keys.hasNext() ) {
			
		    String key = (String)keys.next();
		    if (key.equals("timeSlot")) params.setTimeSlot(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("kafkaEndpoint")) params.setKafkaEndpoint(jsonParams.getString(key).replaceAll("\"", ""));
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
	
	

}
