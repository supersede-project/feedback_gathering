package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;

import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.orchestrator.serialization.ParameterSerializationService;

@Serialize(ParameterSerializationService.class)
public class FeedbackParameter {
	
	private String key;
	private Object value;
	private Object defaultValue;
	private Boolean editableByUser;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String language;
	private Integer id;
	private Integer parentParameterId;
	private Integer mechanismId;
	private Integer pullConfigurationId;
	private Integer genaralConfigurationId;
	
	
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
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
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
	
	public Integer getParentParameterId() {
		return parentParameterId;
	}
	public void setParentParameterId(Integer parentParameterId) {
		this.parentParameterId = parentParameterId;
	}
	public Integer getMechanismId() {
		return mechanismId;
	}
	public void setMechanismId(Integer mechanismId) {
		this.mechanismId = mechanismId;
	}
	public Integer getPullConfigurationId() {
		return pullConfigurationId;
	}
	public void setPullConfigurationId(Integer pullConfigurationId) {
		this.pullConfigurationId = pullConfigurationId;
	}
	public Integer getGenaralConfigurationId() {
		return genaralConfigurationId;
	}
	public void setGenaralConfigurationId(Integer genaralConfigurationId) {
		this.genaralConfigurationId = genaralConfigurationId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
