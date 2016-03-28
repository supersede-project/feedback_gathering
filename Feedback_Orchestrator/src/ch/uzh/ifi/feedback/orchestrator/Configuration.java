package ch.uzh.ifi.feedback.orchestrator;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
	
	private String applicationName;
	private Map<String, FeedbackMechanism> feedbackMechanisms;
	
	public Configuration(){
		
		feedbackMechanisms = new HashMap<>();
		
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public Map<String, FeedbackMechanism> getFeedbackMechanisms() {
		return feedbackMechanisms;
	}

	public void setFeedbackMechanisms(Map<String, FeedbackMechanism> feedbackMechanisms) {
		this.feedbackMechanisms = feedbackMechanisms;
	}
	
}
