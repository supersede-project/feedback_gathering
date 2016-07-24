package ch.uzh.ifi.feedback.orchestrator.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PullConfiguration {
	
	private List<FeedbackParameter> parameters;
	private List<FeedbackMechanism> mechanisms;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Boolean active;
	
	public PullConfiguration(){
		parameters = new ArrayList<>();
		mechanisms = new ArrayList<>();
	}

	public List<FeedbackParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<FeedbackParameter> parameters) {
		this.parameters = parameters;
	}

	public List<FeedbackMechanism> getMechanisms() {
		return mechanisms;
	}

	public void setMechanisms(List<FeedbackMechanism> mechanisms) {
		this.mechanisms = mechanisms;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
