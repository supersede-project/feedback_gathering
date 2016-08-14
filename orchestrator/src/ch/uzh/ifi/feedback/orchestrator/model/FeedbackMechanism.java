package ch.uzh.ifi.feedback.orchestrator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;
import ch.uzh.ifi.feedback.library.rest.validation.Validate;
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.MechanismSerializationService;
import ch.uzh.ifi.feedback.orchestrator.validation.MechanismValidator;

@Validate(MechanismValidator.class)
@Serialize(MechanismSerializationService.class)
public class FeedbackMechanism implements IDbItem{
	
	@NotNull
	@DbAttribute("name")
	private String type;
	@NotNull
	private Boolean active;
	@NotNull
	private Integer order;
	@NotNull
	@DbAttribute("can_be_activated")
	private Boolean canBeActivated;
	@Id
	private Integer id;
	private List<FeedbackParameter> parameters;
	
	public FeedbackMechanism(){
		parameters = new ArrayList<>();
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<FeedbackParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<FeedbackParameter> parameters) {
		this.parameters = parameters;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Boolean isCanBeActivated() {
		return canBeActivated;
	}

	public void setCanBeActivated(Boolean canBeActivated) {
		this.canBeActivated = canBeActivated;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/*
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
*/
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
