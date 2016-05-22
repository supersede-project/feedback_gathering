package ch.uzh.ifi.feedback.orchestrator;

public class FeedbackParameter {
	
	private String key;
	private Object value;
	private Object defaultValue;
	private Boolean editableByUser;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Boolean getEditableByUser() {
		return editableByUser;
	}
	public void setEditableByUser(Boolean editableByUser) {
		this.editableByUser = editableByUser;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!FeedbackParameter.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final FeedbackParameter other = (FeedbackParameter) obj;
	    
	    if((other.getEditableByUser() == null && getEditableByUser() != null) || (other.getEditableByUser() != null && getEditableByUser() == null))
	    	return false;
	    
	    if(other.getEditableByUser() != null && !other.getEditableByUser().equals(getEditableByUser()))
	    	return false;
	    
	    if((other.getDefaultValue() == null && getDefaultValue() != null) || (other.getDefaultValue() != null && getDefaultValue() == null))
	    	return false;
	    
	    if(other.getDefaultValue() != null && !other.getDefaultValue().equals(getDefaultValue()))
	    	return false;
	    
	    return other.getKey().equals(getKey()) && other.getValue().equals(getValue());
	}
}
