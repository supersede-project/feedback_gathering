package ch.uzh.ifi.feedback.orchestrator.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GeneralConfiguration {
	
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private List<FeedbackParameter> parameters;
	
	public GeneralConfiguration()
	{
		parameters = new ArrayList<FeedbackParameter>();
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp created_at) {
		this.createdAt = created_at;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updated_at) {
		this.updatedAt = updated_at;
	}
	public List<FeedbackParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<FeedbackParameter> parameters) {
		this.parameters = parameters;
	}

}
