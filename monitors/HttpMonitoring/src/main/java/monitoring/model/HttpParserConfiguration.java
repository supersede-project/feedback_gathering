package monitoring.model;

import java.util.Iterator;

import org.json.JSONObject;

import monitoring.model.MonitoringParams;
import monitoring.model.ParserConfiguration;

public class HttpParserConfiguration implements ParserConfiguration {

	@Override
	public MonitoringParams parseJsonConfiguration(String jsonConf) {
		HttpMonitoringParams params = new HttpMonitoringParams();
		
		JSONObject jsonParams = new JSONObject(jsonConf);
		jsonParams = jsonParams.getJSONObject("HttpMonitoringConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		params = new HttpMonitoringParams();
		while( keys.hasNext() ) {
		    String key = (String)keys.next();
		    if (key.equals("timeSlot")) params.setTimeSlot(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("toolName")) params.setToolName(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("kafkaEndpoint")) params.setKafkaEndpoint(jsonParams.getString(key).replaceAll("\"", "").replace("http://", ""));
		    else if (key.equals("kafkaTopic")) params.setKafkaTopic(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("url")) params.setUrl(jsonParams.getString(key).replaceAll("\"", ""));
		}
		return params;
	}

}
