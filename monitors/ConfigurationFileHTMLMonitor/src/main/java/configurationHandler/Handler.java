package configurationHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class Handler {
	
	private String monitorResponse;
	private static final String filename = "C:/Users/Panagiotis/Documents/monitor_feedback/monitors/MonitoringUserEvents/PrjMonitoringUserEvents/WebContent/WEB-INF/ConfigurationFile.txt"; 

	public Handler(String monitorResponse) {
		super();
		this.monitorResponse = monitorResponse;
	}
	
	
	public String addConfiguration(String jsonConf) {
		
		try {			
			
			JSONObject json = new JSONObject(jsonConf);
			
			if(!json.has("UserEventsConfProf")){
	    		throw new InvalidJSONException ("A valid UserEventsConfProf JSON file is missing");
	    	}
			
			JSONObject jsonAux = json.getJSONObject("UserEventsConfProf");
			if (!jsonAux.has("toolName")){
		    	throw new InvalidJSONException ("A valid toolName is missing"); 
		    }
			if (!jsonAux.has("kafkaEndpoint")){
		    	throw new InvalidJSONException ("A valid kafkaEndpoint is missing");
		    }
			if (!jsonAux.has("kafkaTopic")){
		    	throw new InvalidJSONException ("A valid kafkaTopic is missing");
		    }
			if (!jsonAux.has("server")){
		    	throw new InvalidJSONException ("A valid server is missing");
		    }
			if (!jsonAux.has("protocol")){
		    	throw new InvalidJSONException ("A valid protocol is missing");
		    }
			if (!jsonAux.has("textContentSize")){
		    	throw new InvalidJSONException ("A valid textContentSize is missing");
		    }
			if (!jsonAux.has("ListOfEvents")){
		    	throw new InvalidJSONException ("A valid ListOfEvents is missing");
		    }
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			bw.write(json.toString());
			bw.close();
			return getResponse(1);
			
		} catch (JSONException e) {
			return throwError("Not a valid JSON configuration object");
		} catch (IOException e) {
			return throwError("An error occurred when I was trying to write the file");
		} catch (Exception e) {
			return throwError(e.getMessage());
		}
		
	}
	
	public String throwError(String error) {
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		try {
			resInfo.put("message", error);
			resInfo.put("status", "error");
			response.put(monitorResponse, resInfo);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response.toString();
	}
	
	public String getResponse(int id) {
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		try {
			resInfo.put("idConf", id);
			resInfo.put("status", "success");
			response.put(monitorResponse, resInfo);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response.toString();
	}
	

}