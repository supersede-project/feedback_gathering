package ch.uzh.ifi.feedback.orchestrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackMechanism {
	
	private String type;
	private boolean active;
	private int order;
	private boolean canBeActivated;
	private List<FeedbackParameter> parameters;
	
	public FeedbackMechanism(){
		parameters = new ArrayList<>();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<FeedbackParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<FeedbackParameter> parameters) {
		this.parameters = parameters;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

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
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!FeedbackMechanism.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final FeedbackMechanism other = (FeedbackMechanism) obj;
	    
	    final List<FeedbackParameter> otherParams = other.getParameters();
	    if(otherParams.size() != getParameters().size())
	    	return false;
	    
	    boolean equals = other.getOrder() == getOrder() 
	    		&& other.getType().equals(getType()) 
	    		&& other.isActive() == isActive() 
	    		&& other.isCanBeActivated() == isCanBeActivated();
	    
	    if(!equals)
	    	return false;
	    
	    for(int i = 0; i < otherParams.size(); i++)
	    {
	    	if(!otherParams.get(i).equals(getParameters().get(i)))
	    		return false;
	    }
	    
	    return true;
	}
}
