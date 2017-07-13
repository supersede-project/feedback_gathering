package monitormanager.service;

import com.google.gson.JsonObject;

public interface IMonitorManager {
	
	JsonObject addConfiguration(String monitorName, JsonObject jsonObj) throws Exception;
	
	JsonObject updateConfiguration(String monitorName, int confId, JsonObject jsonObj) throws Exception;
	
	void deleteConfiguration(String monitorName, int confId) throws Exception;

}
