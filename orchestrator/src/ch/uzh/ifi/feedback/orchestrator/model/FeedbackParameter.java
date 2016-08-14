package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;
import ch.uzh.ifi.feedback.library.rest.validation.Validate;
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.ParameterSerializationService;
import ch.uzh.ifi.feedback.orchestrator.validation.ParameterValidator;

@Validate(ParameterValidator.class)
@Serialize(ParameterSerializationService.class)
public class FeedbackParameter implements IDbItem {
	
	@NotNull
	private String key;
	
	@NotNull
	private Object value;
	
	@DbAttribute("default_value")
	private Object defaultValue;
	@DbAttribute("editable_by_user")
	private Boolean editableByUser;
	@DbAttribute("created_at")
	private Timestamp createdAt;
	@DbAttribute("updated_at")
	private Timestamp updatedAt;
	private String language;
	
	@Id
	private Integer id;

	@DbAttribute("parameters_id")
	private transient Integer parametersId;
	@DbAttribute("mechanism_id")
	private transient Integer mechanismId;
	@DbAttribute("general_configurations_id")
	private transient Integer generalConfigurationsId;
	
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
		return parametersId;
	}
	public void setParentParameterId(Integer parentParameterId) {
		this.parametersId = parentParameterId;
	}
	public Integer getMechanismId() {
		return mechanismId;
	}
	public void setMechanismId(Integer mechanismId) {
		this.mechanismId = mechanismId;
	}
	public Integer getGenaralConfigurationId() {
		return generalConfigurationsId;
	}
	public void setGenaralConfigurationId(Integer genaralConfigurationId) {
		this.generalConfigurationsId = genaralConfigurationId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
/*	
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
	*/
}
