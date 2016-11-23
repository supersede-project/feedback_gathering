package ch.uzh.ifi.feedback.orchestrator.model;

public abstract class MonitorConfigurationParams {
	
	private Integer id;
		
	private String timeSlot;

	private String kafkaEndpoint;
	
	private String kafkaTopic;
	
	private String state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getConfId() {
		return id.toString();
	}
	
	public void setConfId(String id) {
		this.id = Integer.valueOf(id);
	}
	
	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
