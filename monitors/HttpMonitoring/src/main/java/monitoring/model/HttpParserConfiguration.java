package monitoring.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import monitoring.model.MonitoringParams;
import monitoring.model.ParserConfiguration;

public class HttpParserConfiguration implements ParserConfiguration {
	
	MultipartFile file;

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
		    else if (key.equals("method")) params.setMethod(Method.valueOf(jsonParams.getString(key).replaceAll("\"", "")));
		    else if (key.equals("body")) params.setBody(jsonParams.getJSONObject(key));
		    else if (key.equals("headers")) {
		    	JSONObject headersJson= jsonParams.getJSONObject("headers");
		    	Map<String,String> headers = new HashMap<>();
		    	for (String s : headersJson.keySet()) {
		    		headers.put(s, headersJson.getString(s));
		    	}
		    	params.setHeaders(headers);
		    }
		}
		if (file != null) params.setFile(file);
		
		return params;
	}
	
	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
