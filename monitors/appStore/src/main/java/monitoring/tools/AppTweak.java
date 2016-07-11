package monitoring.tools;

import kafka.javaapi.producer.Producer;
import monitoring.model.MonitoringParams;
import monitoring.services.ToolInterface;

public class AppTweak implements ToolInterface {

	@Override
	public void addConfiguration(MonitoringParams params, Producer<String, String> producer) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
