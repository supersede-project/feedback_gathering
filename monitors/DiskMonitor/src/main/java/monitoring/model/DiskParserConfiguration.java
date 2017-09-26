package monitoring.model;

import java.util.Iterator;

import org.json.JSONObject;

import monitoring.model.MonitoringParams;
import monitoring.model.ParserConfiguration;

public class DiskParserConfiguration implements ParserConfiguration {

	@Override
	public MonitoringParams parseJsonConfiguration(String jsonConf) {
		DiskMonitoringParams params = new DiskMonitoringParams();
		
		JSONObject jsonParams = new JSONObject(jsonConf);
		jsonParams = jsonParams.getJSONObject("DiskMonitoringConfProf");
		
		Iterator<?> keys = jsonParams.keys();
		params = new DiskMonitoringParams();
		while( keys.hasNext() ) {
		    String key = (String)keys.next();
		    if (key.equals("timeSlot")) params.setTimeSlot(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("toolName")) params.setToolName(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("kafkaEndpoint")) params.setKafkaEndpoint(jsonParams.getString(key).replaceAll("\"", "").replace("http://", ""));
		    else if (key.equals("kafkaTopic")) params.setKafkaTopic(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("label")) params.setLabel(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("user")) params.setUser(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("host")) params.setHost(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("instruction")) params.setInstruction(jsonParams.getString(key).replaceAll("\"", ""));
		}
		return params;
	}

}
