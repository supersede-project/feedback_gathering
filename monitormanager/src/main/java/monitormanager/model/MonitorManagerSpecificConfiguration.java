package monitormanager.model;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = MonitorConfigurationDeserializer.class)
public class MonitorManagerSpecificConfiguration {
	@JsonIgnore
	Integer id;
	String toolName;
	@JsonSerialize(using = ToStringSerializer.class)
	Integer timeSlot;
	URL kafkaEndpoint;
	String kafkaTopic;
	
	public String getToolName() {
		return toolName;
	}
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	public Integer getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(Integer timeSlot) {
		this.timeSlot = timeSlot;
	}
	public URL getKafkaEndpoint() {
		return kafkaEndpoint;
	}
	public void setKafkaEndpoint(URL kafkaEndpoint) {
		this.kafkaEndpoint = kafkaEndpoint;
	}
	public String getKafkaTopic() {
		return kafkaTopic;
	}
	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}