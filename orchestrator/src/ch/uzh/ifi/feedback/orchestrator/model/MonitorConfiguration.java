package ch.uzh.ifi.feedback.orchestrator.model;

import java.util.HashMap;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorTypeSerializationService;

@Serialize(MonitorConfigurationSerializationService.class)
public class MonitorConfiguration extends OrchestratorItem<MonitorConfiguration> {

	@Id
	@DbAttribute("monitor_configuration_id")
	private Integer id;
	
	@DbAttribute("monitor_tool_name")
	private String monitorToolName;
	
	public String getMonitorToolName() {
		return monitorToolName;
	}

	public void setMonitorToolName(String monitorToolName) {
		this.monitorToolName = monitorToolName;
	}

	@NotNull
	private String kafkaEndpoint;
	
	@NotNull
	private String kafkaTopic;
	
	@NotNull
	private int timeSlot;
	
	private HashMap<String,String> params;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
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
	
	public Integer getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(Integer timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	public String getParam(String key) {
		return params.get(key);
	}
	
	public HashMap<String, String> getParams() {
		return params;
	}
	
	public void setParam(String key, String value) {
		params.put(key, value);
	}

}
