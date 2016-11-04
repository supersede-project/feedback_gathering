package ch.uzh.ifi.feedback.orchestrator.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitoringConfigurationParamsSerializationService;

@Serialize(MonitoringConfigurationParamsSerializationService.class)
public abstract class MonitorConfigurationParams extends OrchestratorItem<MonitorConfigurationParams> {
	
	@Id
	@DbAttribute("monitor_configuration_params_id")
	private Integer intId;
	
	private String id;
	
	private String timeSlot;

	private String kafkaEndpoint;
	
	private String kafkaTopic;
	
	private String state;

	@Override
	public Integer getId() {
		return intId;
	}

	@Override
	public void setId(Integer id) {
		this.intId = id;
	}
	
	public String getConfId() {
		return id;
	}
	
	public void setConfId(String id) {
		this.id = id;
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
