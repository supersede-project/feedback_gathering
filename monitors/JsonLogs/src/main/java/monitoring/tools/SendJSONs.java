package monitoring.tools;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.apache.log4j.Logger;
import org.json.JSONObject;

import monitoring.kafka.KafkaCommunication;
import monitoring.model.JsonLogsMonitoringData;

public class SendJSONs {
	
	final static Logger logger = Logger.getLogger(SendJSONs.class);
	
	//Data object instances	
	int id = 1;
	int configurationId = 0;
	
	KafkaCommunication kafka;
	
	public SendJSONs() {
		super();
	}


	public void generateData(String json, String kafkaTopic) throws Exception {
		
		kafka = new KafkaCommunication(null);
		
		String timestamp;
		
		List<JsonLogsMonitoringData> data = new ArrayList<>();
		
		try{
			
			JsonLogsMonitoringData params = new JsonLogsMonitoringData();
			
			JSONObject jsonParams = new JSONObject(json);
				
			Iterator<?> keys = jsonParams.keys();
			while( keys.hasNext() ) {
				String key = (String)keys.next();
				if (key.equals("serial_no")) params.setSerial_no(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("Date")) params.setDate(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("level")) params.setLevel(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("type")) params.setType(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("class_name")) params.setClass_name(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("method_name")) params.setMethod_name(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("line_number")) params.setLine_number(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("Location")) params.setLocation(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("message")) params.setMessage(jsonParams.getString(key).replaceAll("\"", ""));
				else if (key.equals("sessionID")) params.setSessionID(jsonParams.getString(key).replaceAll("\"", ""));
			}
				
			data.add(params);
			JSONObject json_obj = params.toJsonObject();
			logger.debug("Sent data: " + json_obj.toString());
				
			
			timestamp = new Timestamp((new Date()).getTime()).toString();
			kafka.sendData(data, timestamp, id, configurationId, kafkaTopic, "JSONFiles");
			logger.debug("Data successfully sent to Kafka endpoint");
			++id;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}

