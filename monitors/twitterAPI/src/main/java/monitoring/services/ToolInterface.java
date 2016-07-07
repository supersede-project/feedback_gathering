package monitoring.services;

import kafka.javaapi.producer.Producer;
import monitoring.params.MonitoringParams;

public interface ToolInterface {
	
	public void addConfiguration(MonitoringParams params, Producer<String,String> producer) throws Exception;

}
