package monitormanager.service;

import monitormanager.model.MonitorManagerSpecificConfiguration;
import monitormanager.model.MonitorManagerSpecificConfigurationResult;

public interface IMonitorManager {
	
	MonitorManagerSpecificConfigurationResult addConfiguration(String monitorName, MonitorManagerSpecificConfiguration input) throws Exception;
	
	MonitorManagerSpecificConfigurationResult updateConfiguration(String monitorName, int confId, MonitorManagerSpecificConfiguration input) throws Exception;
	
	void deleteConfiguration(String monitorName, int confId) throws Exception;

}
