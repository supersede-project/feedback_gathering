package monitoring.model;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONObject;

public class Utils {

	public static MonitoringParams parseJsonConfiguration(String json) {
		
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
	
	public static String streamToString(InputStream stream) {
		StringBuilder sb = new StringBuilder();
		try (Scanner scanner = new Scanner(stream)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    sb.append(responseBody);
		}
		return sb.toString();
	}
	
	public static long getDateTimeInMillis(JSONObject obj) {
		return obj.getJSONArray("comments").getJSONObject(0).getJSONObject("userComment")
		.getJSONObject("lastModified").getInt("nanos")/1000000 + 
		obj.getJSONArray("comments").getJSONObject(0).getJSONObject("userComment")
		.getJSONObject("lastModified").getLong("seconds")*1000;
	}
	
}
