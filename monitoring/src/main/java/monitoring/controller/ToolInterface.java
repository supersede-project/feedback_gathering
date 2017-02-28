package monitoring.controller;

import monitoring.model.MonitoringParams;

public interface ToolInterface<T extends MonitoringParams> {

	public void addConfiguration(T params, int confId) throws Exception;
	
	public void deleteConfiguration() throws Exception;

	public void updateConfiguration(T params) throws Exception;
	
}
