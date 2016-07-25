package monitoring.services;

import kafka.javaapi.producer.Producer;
import monitoring.model.MonitoringParams;

public interface ToolInterface {

	public void addConfiguration(MonitoringParams params, Producer<String,String> producer, int confId) throws Exception;
	
	public void deleteConfiguration() throws Exception;
	
}
