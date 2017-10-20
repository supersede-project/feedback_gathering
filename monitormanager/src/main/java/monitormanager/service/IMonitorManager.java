package monitormanager.service;

import monitormanager.model.ConfigurationResponse;

public interface IMonitorManager {
	
	ConfigurationResponse addConfiguration(String monitorName, String input) throws Exception;
	
	ConfigurationResponse updateConfiguration(String monitorName, int confId, String input) throws Exception;
	
	void deleteConfiguration(String monitorName, int confId) throws Exception;

}
