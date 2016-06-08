package monitoring.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import monitoring.params.MonitoringData;
import monitoring.params.MonitoringParams;

public abstract class ServiceWrapper {
	
	//The id of the block of data sent to kafka endpoint
	private int responseDataId = 1;
	//The id of the response associated to a addConfiguration call
	private int responseId = 1;
	
	//An instance object with the configuration parameters
	MonitoringParams params;
	
	//An instance of the producer that communicates with the kafka endpoint
	Producer<String, String> producer;
	
	/**
	 * Inits the Twitter monitoring with the configuration received. 
	 * Implemented for each service
	 * 
	 * @param jsonConf			string with the json configuration
	 * @return					string response of the API call
	 */
	public abstract String addConfiguration(String jsonConf);
	
	/**
	 * Returns a JSON response with the response of the API
	 * @param status			"success" or "error"
	 * @return					JSONObject response of the API call
	 */
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
	
	/**
	 * Parses the monitored data into a JSONObject
	 * @param timeStamp			the timeStamp of the monitoring search
	 * @param data				a list with the tweets info
	 */
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
	
	/**
	 * Sends the JSON data object to the kafka endpoint
	 * @param data			the JSONObject to send to the endpoint
	 */
	private void sendToKafka(JSONObject data) {

		KeyedMessage<String, String> msg = new KeyedMessage<String, String>(params.getKafkaTopic(), data.toString());
		producer.send(msg);
		
	}
	
	/**
	 * Initializes the producer configuration with the specific kafka endpoint
	 */
	public void initKafka() {
		Properties props = new Properties();
		 
		props.put("metadata.broker.list", params.getKafkaEndpoint().replace("http://", ""));
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		 
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<String, String>(config);
	}

	/**
	 * Parses the given string into an instance of the MonitoringParams object
	 * @param json				string with the monitor configuration
	 * @throws JSONException
	 */
	public void parseConfigurationParams(String json) throws JSONException {
		
		JSONObject jsonParams = new JSONObject(json);
		jsonParams = jsonParams.getJSONObject("SocialNetworksMonitoringConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		params = new MonitoringParams();

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
		
	}
	
	

}
