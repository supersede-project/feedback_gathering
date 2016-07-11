package monitoring.model;

public class MonitoringParams {

	private String toolName;
	private String timeSlot;
	private String kafkaEndpoint;
	private String kafkaTopic;
	private String appId;
	
	public String getToolName() {
		return toolName;
	}
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	public String getKafkaEndpoint() {
		return kafkaEndpoint;
	}
	public void setKafkaEndpoint(String kafkaEndpoint) {
		this.kafkaEndpoint = kafkaEndpoint;
	}
	public String getKafkaTopic() {
		return kafkaTopic;
	}
	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	
}
