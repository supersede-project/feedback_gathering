package ch.uzh.ifi.feedback.orchestrator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.library.rest.annotations.Validate;
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.validation.ApplicationValidator;

@Validate(ApplicationValidator.class)
@Serialize(ApplicationSerializationService.class)
public class Application extends OrchestratorItem<Application> {
	
	@Id
	@DbAttribute("applications1_id")
	private Integer id;
	
	@NotNull
	@Unique
	private String name;
	private Integer state;
	@DbIgnore
	private GeneralConfiguration generalConfiguration;
	@DbIgnore
	private List<Configuration> configurations;
	
	@DbAttribute("general_configurations_id")
	private transient Integer generalConfigurationId;
	
	public Application()
	{
		configurations = new ArrayList<>();
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		
		if(generalConfiguration != null){
			generalConfiguration.Merge(original.getGeneralConfiguration());
		}else{
			generalConfiguration = original.getGeneralConfiguration();
		}
		
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
		
		super.Merge(original);
		
		return this;
	}
}
