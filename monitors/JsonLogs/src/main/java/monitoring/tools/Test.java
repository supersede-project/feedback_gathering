package monitoring.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONObject;

import monitoring.model.JsonLogsMonitoringData;

public class Test {
	
	 public static void main(String args[]) {

		 String input = "";
		 String filename = "C:/Users/Panagiotis/Downloads/ecosys_debug_json.json";
			
		 List<JsonLogsMonitoringData> data = new ArrayList<>();
			
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
				    //System.out.println(input);
					
					
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
					System.out.println("Sent data: " + json_obj.toString());
					
					
				}
				scanner.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
	 }

}
