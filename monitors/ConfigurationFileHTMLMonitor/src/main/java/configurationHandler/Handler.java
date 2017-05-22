package configurationHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

public class Handler {
	
	private String monitorResponse;  
	
	public Handler(String monitorResponse) {
		super();
		this.monitorResponse = monitorResponse;
	}
	
	
	public String addConfiguration(String jsonConf)  {
		
				
		try {			
			
			JSONObject json = new JSONObject(jsonConf);
			
			if(!json.has("UserEventsConfProf")){
				return throwError ("A valid UserEventsConfProf JSON file is missing");
	    	}
			
			JSONObject jsonAux = json.getJSONObject("UserEventsConfProf");
			if (!jsonAux.has("toolName")){
				return throwError ("A valid toolName is missing"); 
		    }
			if (!jsonAux.has("kafkaEndpoint")){
				return throwError ("A valid kafkaEndpoint is missing");
		    }
			if (!jsonAux.has("kafkaTopic")){
				return throwError ("A valid kafkaTopic is missing");
		    }
			if (!jsonAux.has("server")){
				return throwError ("A valid server is missing");
		    }
			if (!jsonAux.has("protocol")){
				return throwError ("A valid protocol is missing");
		    }
			if (!jsonAux.has("textContentSize")){
				return throwError ("A valid textContentSize is missing");
		    }
			if (!jsonAux.has("ListOfEvents")){
				return throwError ("A valid ListOfEvents is missing");
		    }
			
			//the properties file contains the absolute path where the configuration file of the HTML monitor is going to be saved
			InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(input);
			String filename = prop.getProperty("path");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			bw.write(json.toString());
			bw.close();
			return getResponse(1);
			
		} catch (JSONException e) {
			return throwError("Not a valid JSON configuration object");
		} catch (IOException e) {
			return throwError("An error occurred when I tried to write the file");
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