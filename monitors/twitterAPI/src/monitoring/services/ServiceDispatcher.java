package monitoring.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import monitoring.params.MonitoringParams;

@Path("SocialNetworkMonitoring")
public class ServiceDispatcher {
	
	//The id of the response associated to a addConfiguration call
	private int responseId = 1;
	
	@POST
	@Path("/configuration")
	public String addConfiguration(@QueryParam("configurationJson") String jsonConf) {
				
		try {
			
			MonitoringParams params = parseJsonConfiguration(jsonConf);
			
			//Hardcoded monitor name for testing purposes
			Class monitor = Class.forName(TwitterAPI.class.getName());
			ServiceInterface serviceInstance = (ServiceInterface) monitor.newInstance();
			
			//Initializes kafka producer
			Properties props = new Properties();
			props.put("metadata.broker.list", params.getKafkaEndpoint());
			props.put("serializer.class", "kafka.serializer.StringEncoder");
			props.put("request.required.acks", "1");
			 
			ProducerConfig config = new ProducerConfig(props);
			serviceInstance.addConfiguration(params, new Producer<String,String>(config));
			
		} catch (JSONException e) {
			return throwError("Not a valid JSON configuration object");
		} catch (ClassNotFoundException e) {
			return throwError("Not existing monitor");
		} catch (InstantiationException e) {
			return throwError("Monitor class must be concrete");
		} catch (IllegalAccessException e) {
			return throwError("Monitor class must have a constructor with no args");
		} catch (Exception e) {
			return throwError(e.getMessage());
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
			resInfo.put("idConf", responseId);
			resInfo.put("status", "error");
			resInfo.put("message", error);
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
