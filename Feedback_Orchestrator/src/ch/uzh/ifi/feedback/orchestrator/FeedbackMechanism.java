package ch.uzh.ifi.feedback.orchestrator;

import java.util.HashMap;
import java.util.Map;

public class FeedbackMechanism {
	
	private boolean active;
	private int order;
	private Map<String, Object> parameters;
	
	public FeedbackMechanism(){
		parameters = new HashMap<>();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
