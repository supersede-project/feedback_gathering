package ch.uzh.ifi.feedback.orchestrator;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
import java.util.HashMap;
import java.util.Map;
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907

public class Configuration {
	
	private String applicationName;
<<<<<<< HEAD
	private List<FeedbackMechanism> feedbackMechanisms;
	
	public Configuration(){
		
		feedbackMechanisms = new ArrayList<>();
=======
	private Map<String, FeedbackMechanism> feedbackMechanisms;
	
	public Configuration(){
		
		feedbackMechanisms = new HashMap<>();
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
		
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

<<<<<<< HEAD
	public List<FeedbackMechanism> getFeedbackMechanisms() {
		return feedbackMechanisms;
	}

	public void setFeedbackMechanisms(List<FeedbackMechanism> feedbackMechanisms) {
=======
	public Map<String, FeedbackMechanism> getFeedbackMechanisms() {
		return feedbackMechanisms;
	}

	public void setFeedbackMechanisms(Map<String, FeedbackMechanism> feedbackMechanisms) {
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
		this.feedbackMechanisms = feedbackMechanisms;
	}
	
}
