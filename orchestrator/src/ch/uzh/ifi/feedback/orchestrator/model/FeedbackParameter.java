package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.Service.ItemBase;
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
public class FeedbackParameter extends OrchestratorItem<FeedbackParameter> {
	
	@Id
	@DbAttribute("parameters_id")
	private Integer id;
	
	@NotNull
	private String key;

	private Object value;
	
	@DbAttribute("default_value")
	private Object defaultValue;
	@DbAttribute("editable_by_user")
	private Boolean editableByUser;

	private String language;

	@DbAttribute("parent_parameters_id")
	private transient Integer parametersId;
	@DbAttribute("mechanisms_id")
	private transient Integer mechanismId;
	@DbAttribute("general_configurations_id")
	private transient Integer generalConfigurationsId;
	
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
	
	@Override
	public FeedbackParameter Merge(FeedbackParameter original) {
		if(List.class.isAssignableFrom(original.getValue().getClass()))
		{
			List<FeedbackParameter> oldChildren = (List<FeedbackParameter>)original.getValue();
			if(List.class.isAssignableFrom(this.getValue().getClass()))
			{
				List<FeedbackParameter> newChildren = (List<FeedbackParameter>)this.getValue();
				for(FeedbackParameter param : oldChildren)
				{
					Optional<FeedbackParameter> newParam = newChildren.stream().filter(p -> p.getId().equals(param.getId())).findFirst();
					if(!newParam.isPresent())
					{
						newChildren.add(param);
					}else{ 
						newParam.get().Merge(param);
					}
				}
			}
		}
		
		super.Merge(original);
		
		return this;
	}
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
