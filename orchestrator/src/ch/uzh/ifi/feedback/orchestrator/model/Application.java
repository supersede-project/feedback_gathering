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
import ch.uzh.ifi.feedback.orchestrator.validation.ApplicationValidator;

@Validate(ApplicationValidator.class)
@Serialize(ApplicationSerializationService.class)
public class Application extends ItemBase<Application> {
	
	@NotNull
	@Unique
	private String name;
	@DbAttribute("created_at")
	private Timestamp createdAt;
	private Integer state;
	private GeneralConfiguration generalConfiguration;
	private List<Configuration> configurations;
	
	@DbAttribute("general_configuration_id")
	private transient Integer generalConfigurationId;
	
	public Application()
	{
		configurations = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

	public List<Configuration> getConfigurations() {
		if (configurations == null)
			return new ArrayList<>();
		
		return configurations;
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
	public Application Merge(Application original) {
		super.Merge(original);
		
		for(Configuration config : original.getConfigurations())
		{
			Optional<Configuration> newConfig = getConfigurations().stream().filter(p -> p.getId().equals(config.getId())).findFirst();
			if(!newConfig.isPresent())
			{
				configurations.add(config);
			}else{ 
				newConfig.get().Merge(config);
			}
		}
		
		if(generalConfiguration != null){
			generalConfiguration.Merge(original.getGeneralConfiguration());
		}else{
			generalConfiguration = original.getGeneralConfiguration();
		}
		
		return this;
	}
}
