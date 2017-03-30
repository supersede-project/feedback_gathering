package monitoring.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import monitoring.model.MonitoringParams;
import monitoring.model.ParserConfiguration;

public class TwitterParserConfiguration implements ParserConfiguration {

	@Override
	public MonitoringParams parseJsonConfiguration(String jsonConf) {
		TwitterMonitoringParams params = new TwitterMonitoringParams();
		
		JSONObject jsonParams = new JSONObject(jsonConf);
		jsonParams = jsonParams.getJSONObject("SocialNetworksMonitoringConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		params = new TwitterMonitoringParams();
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

}
