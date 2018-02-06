package monitoring.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
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
		    else if (key.equals("user")) params.setUser(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("host")) params.setHost(jsonParams.getString(key).replaceAll("\"", ""));
		    else if (key.equals("instructions")) {
		    	List<Instruction> instructions = new ArrayList<>();
		    	JSONArray array = jsonParams.getJSONArray("instructions");
		    	for (int i = 0; i < array.length(); ++i) {
		    		Instruction instruction = new Instruction(array.getJSONObject(i).getString("instruction").replaceAll("\"", ""),
		    				array.getJSONObject(i).getString("label").replaceAll("\"", ""));
		    		instructions.add(instruction);
		    	}
		    	params.setInstructions(instructions);
		    }
		}
		return params;
	}

}
