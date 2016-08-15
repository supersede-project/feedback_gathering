package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.Service.ItemBase;
import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;
import ch.uzh.ifi.feedback.library.rest.validation.Unique;
import ch.uzh.ifi.feedback.library.rest.validation.Validate;
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.ConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.validation.ConfigurationValidator;

@Validate(ConfigurationValidator.class)
@Serialize(ConfigurationSerializationService.class)
public class Configuration extends ItemBase<Configuration> {
	
	@Unique
	private String name;

	@DbAttribute("created_at")
	private Timestamp createdAt;
	@NotNull
	private ConfigurationType type;
	private List<FeedbackMechanism> mechanisms;
	private GeneralConfiguration generalConfiguration;
	
	@DbAttribute("general_configuration_id")
	private transient Integer generalConfigurationId;
	@DbAttribute("application_id")
	private transient Integer applicationId;
	
	public Configuration(){
		mechanisms = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String applicationName) {
		this.name = applicationName;
	}

	public List<FeedbackMechanism> getFeedbackMechanisms() {
		if (mechanisms == null)
			mechanisms = new ArrayList<>();
		
		return mechanisms;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public ConfigurationType getType() {
		return type;
	}

	public void setType(ConfigurationType type) {
		this.type = type;
	}

	public GeneralConfiguration getGeneralConfiguration() {
		return generalConfiguration;
	}

	public void setGeneralConfiguration(GeneralConfiguration generalConfiguration) {
		this.generalConfiguration = generalConfiguration;
	}

	public Integer getGeneralConfigurationId() {
		return generalConfigurationId;
	}

	public void setGeneralConfigurationId(Integer generalConfigurationId) {
		this.generalConfigurationId = generalConfigurationId;
	}
	
	@Override
	public Configuration Merge(Configuration original) {
		super.Merge(original);
		
		for(FeedbackMechanism mechanism : original.getFeedbackMechanisms())
		{
			Optional<FeedbackMechanism> newMechanism = getFeedbackMechanisms().stream().filter(p -> p.getId().equals(mechanism.getId())).findFirst();
			if(!newMechanism.isPresent())
			{
				mechanisms.add(mechanism);
			}else{ 
				newMechanism.get().Merge(mechanism);
			}
		}
		
		if(generalConfiguration != null){
			generalConfiguration.Merge(original.getGeneralConfiguration());
		}else{
			generalConfiguration = original.getGeneralConfiguration();
		}
		
		return this;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
}
