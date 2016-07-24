package ch.uzh.ifi.feedback.orchestrator.Model;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
	
	private String applicationName;
	private List<FeedbackMechanism> feedbackMechanisms;
	
	public Configuration(){
		feedbackMechanisms = new ArrayList<>();
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public List<FeedbackMechanism> getFeedbackMechanisms() {
		return feedbackMechanisms;
	}

	public void setFeedbackMechanisms(List<FeedbackMechanism> feedbackMechanisms) {
		this.feedbackMechanisms = feedbackMechanisms;
	}
	
}
