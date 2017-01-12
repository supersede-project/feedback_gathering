package monitormanager.service;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import monitormanager.model.ConfigurationParser;

@RestController
@RequestMapping("/configuration")
public class RESTController {
	
	private ConfigurationParser parser = new ConfigurationParser();

	@RequestMapping(method = RequestMethod.POST)
	public String addConfiguration(@RequestBody String input) {
		
		JsonObject jsonObj = getJson(input);
		JsonObject configuration = null;
		
		switch (jsonObj.get("monitor").getAsString()) {
			case "TwitterAPI":
				configuration = parser.getTwitterConfiguration(jsonObj);
				break;
			case "GooglePlay":
				configuration = parser.getGooglePlayConfiguration(jsonObj);
				break;
			case "AppStore":
				configuration = parser.getAppStoreConfiguration(jsonObj);
				break;
		}
		
		//TODO call monitor
		
		return getResponse();
		
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public String updateConfiguration(@RequestParam("id") String id, @RequestParam("monitor") String monitor, @RequestBody String input) {
		
		JsonObject jsonObj = getJson(input);
		JsonObject configuration = null;
		
		switch (jsonObj.get("monitor").getAsString()) {
			case "TwitterAPI":
				configuration = parser.getTwitterConfiguration(jsonObj);
				break;
			case "GooglePlay":
				configuration = parser.getGooglePlayConfiguration(jsonObj);
				break;
			case "AppStore":
				configuration = parser.getAppStoreConfiguration(jsonObj);
				break;
		}
		
		//TODO call monitor
		
		return getResponse();
		
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public String deleteConfiguration(@RequestParam("id") String id, @RequestParam("monitor") String monitor) {
		
		//TODO call monitor
		
		return getResponse();
		
	}

	private JsonObject getJson(String configuration) {
		JsonParser jsonParser = new JsonParser();
		JsonObject json = (JsonObject)jsonParser.parse(configuration);
		return json;
	}
	
	private String getResponse() {
		JSONObject resInfo = new JSONObject();
		
		try {
			resInfo.put("status", "success");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return resInfo.toString();
	}
	
}
