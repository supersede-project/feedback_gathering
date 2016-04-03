package ch.uzh.ifi.feedback.orchestrator;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
=======
import java.util.HashMap;
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
import java.util.Map;

public class FeedbackMechanism {
	
<<<<<<< HEAD
	private String type;
	private boolean active;
	private int order;
	private boolean canBeActivated;
	private List<FeedbackParameter> parameters;
	
	public FeedbackMechanism(){
		parameters = new ArrayList<>();
=======
	private boolean active;
	private int order;
	private Map<String, Object> parameters;
	
	public FeedbackMechanism(){
		parameters = new HashMap<>();
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

<<<<<<< HEAD
	public List<FeedbackParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<FeedbackParameter> parameters) {
=======
	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
		this.parameters = parameters;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
<<<<<<< HEAD

	public boolean isCanBeActivated() {
		return canBeActivated;
	}

	public void setCanBeActivated(boolean canBeActivated) {
		this.canBeActivated = canBeActivated;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
=======
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
}
