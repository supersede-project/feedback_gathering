package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Application {
	
	private String name;
	private Timestamp createdAt;
	private Integer state;
	private Integer id;
	private List<GeneralConfiguration> general_configurations;
	private List<PullConfiguration> pull_configurations;
	private List<FeedbackMechanism> mechanisms;
	
	public Application()
	{
		general_configurations = new ArrayList<>();
		pull_configurations = new ArrayList<>();
		mechanisms = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public List<GeneralConfiguration> getGeneralConfigurations() {
		return general_configurations;
	}
	public void setGeneralConfigurations(List<GeneralConfiguration> generalConfigurations) {
		this.general_configurations = generalConfigurations;
	}

	public List<PullConfiguration> getPullConfigurations() {
		return pull_configurations;
	}

	public void setPullConfigurations(List<PullConfiguration> pullConfigurations) {
		this.pull_configurations = pullConfigurations;
	}

	public List<FeedbackMechanism> getFeedbackMechanisms() {
		return mechanisms;
	}

	public void setFeedbackMechanisms(List<FeedbackMechanism> feedbackMechanisms) {
		this.mechanisms = feedbackMechanisms;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
