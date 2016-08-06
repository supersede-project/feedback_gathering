package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
	
	private String name;
	private Integer id;
	private Timestamp createdAt;
	private ConfigurationType type;
	private List<FeedbackMechanism> feedbackMechanisms;
	private List<GeneralConfiguration> generalConfigurations;
	
	public Configuration(){
		feedbackMechanisms = new ArrayList<>();
		generalConfigurations = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String applicationName) {
		this.name = applicationName;
	}

	public List<FeedbackMechanism> getFeedbackMechanisms() {
		return feedbackMechanisms;
	}

	public List<GeneralConfiguration> getGeneralConfigurations()
	{
		return this.generalConfigurations;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public ConfigurationType getType() {
		return type;
	}

	public void setType(ConfigurationType type) {
		this.type = type;
	}
	
}
