package monitormanager.service;

import com.google.gson.JsonObject;

public interface IMonitorManager {
	
	JsonObject addConfiguration(String monitorName, String input) throws Exception;
	
	JsonObject updateConfiguration(String monitorName, int confId, String input) throws Exception;
	
	void deleteConfiguration(String monitorName, int confId) throws Exception;

}
