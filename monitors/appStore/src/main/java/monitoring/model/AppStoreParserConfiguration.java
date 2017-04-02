package monitoring.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import monitoring.model.MonitoringParams;
import monitoring.model.ParserConfiguration;

public class AppStoreParserConfiguration implements ParserConfiguration {

	@Override
	public MonitoringParams parseJsonConfiguration(String jsonConf) {
		AppStoreMonitoringParams params = new AppStoreMonitoringParams();
		
		JSONObject jsonParams = new JSONObject(jsonConf);
		jsonParams = jsonParams.getJSONObject("AppStoreConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		params = new AppStoreMonitoringParams();
		while( keys.hasNext() ) {
		    String key = (String)keys.next();
		    if (key.equals("timeSlot")) params.setTimeSlot(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("toolName")) params.setToolName(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("kafkaEndpoint")) params.setKafkaEndpoint(jsonParams.getString(key).replaceAll("\"", "").replace("http://", ""));
		    else if (key.equals("kafkaTopic")) params.setKafkaTopic(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("appId")) params.setAppId(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("country")) params.setCountry(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("language")) params.setLanguage(jsonParams.getString(key).replaceAll("\"", ""));
		}
		return params;
	}

}
