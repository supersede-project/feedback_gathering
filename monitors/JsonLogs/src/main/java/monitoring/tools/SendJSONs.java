package monitoring.tools;


import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import monitoring.kafka.KafkaCommunication;
import monitoring.model.JsonLogsMonitoringData;

public class SendJSONs {
	
	final static Logger logger = Logger.getLogger(SendJSONs.class);
	
	//Data object instances	
	int id = 1;
	int configurationId = 0;
	
	KafkaCommunication kafka = new KafkaCommunication();	
	
	
	public SendJSONs() {
		super();
	}


	public void generateData(String filename, String kafkaEndPoint, String kafkaTopic) throws Exception {
		
		//logger.debug("Initialising kafka producer...");
		//kafka.initProducer(kafkaEndPoint);
		logger.debug("Initialising proxy...");
		kafka.initProxy(kafkaEndPoint);
		
		String timestamp;
		
		List<JsonLogsMonitoringData> data = new ArrayList<>();		
		
		String input = "";
		
		try{
			Scanner scanner = new Scanner(new File(filename));
			scanner.useDelimiter(Pattern.compile("}"));

			while(scanner.hasNext()) {
				input = scanner.next().trim();
				if (!input.equals("")){
					input+="\n}";
				}
				else{
					continue;
				}				
				
				JsonLogsMonitoringData params = new JsonLogsMonitoringData();
				
				JSONObject jsonParams = new JSONObject(input);
				
				Iterator<?> keys = jsonParams.keys();
				while( keys.hasNext() ) {
				    String key = (String)keys.next();
				    if (key.equals("timeMillis")) params.setTimeMillis(jsonParams.getBigInteger(key));
				    else if (key.equals("thread")) params.setThread(jsonParams.getString(key).replaceAll("\"", ""));
				    else if (key.equals("level")) params.setLevel(jsonParams.getString(key).replaceAll("\"", ""));
				    else if (key.equals("loggerName")) params.setLoggerName(jsonParams.getString(key).replaceAll("\"", ""));
				    else if (key.equals("message")) params.setMessage(jsonParams.getString(key).replaceAll("\"", ""));
				    else if (key.equals("endOfBatch")) params.setEndOfBatch(jsonParams.getBoolean(key));
				    else if (key.equals("loggerFqcn")) params.setLoggerFqcn(jsonParams.getString(key).replaceAll("\"", ""));
				    else if (key.equals("threadId")) params.setThreadId(jsonParams.getInt(key));
				    else if (key.equals("threadPriority")) params.setThreadPriority(jsonParams.getInt(key));
				}
				
				
				data.add(params);
				JSONObject json_obj = params.toJsonObject();
				logger.debug("Sent data: " + json_obj.toString());
				
				
			}
			scanner.close();
			
			timestamp = new Timestamp((new Date()).getTime()).toString();
			//kafka.generateResponseKafka(data, timestamp, id, configurationId, kafkaTopic, "JSONFiles");
			kafka.generateResponseIF(data, timestamp, id, configurationId, kafkaTopic, "JSONFiles");
			logger.debug("Data successfully sent to Kafka endpoint");
			++id;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}

