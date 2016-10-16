package ch.uzh.ifi.feedback.orchestrator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.library.rest.annotations.Validate;
import ch.uzh.ifi.feedback.orchestrator.serialization.GeneralConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.validation.GeneralConfigurationValidator;

@Validate(GeneralConfigurationValidator.class)
@Serialize(GeneralConfigurationSerializationService.class)
public class GeneralConfiguration extends OrchestratorItem<GeneralConfiguration>{
	
	@Id
	@DbAttribute("general_configurations_id")
	private Integer id;
	@DbIgnore
	private List<FeedbackParameter> parameters;
	@Unique
	private String name;
	
	public GeneralConfiguration()
	{
		parameters = new ArrayList<FeedbackParameter>();
	}
	
	public List<FeedbackParameter> getParameters() {
		if (parameters == null)
			parameters = new ArrayList<>();
		
		return parameters;
	}
	public void setParameters(List<FeedbackParameter> parameters) {
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public GeneralConfiguration Merge(GeneralConfiguration original) {
		for(FeedbackParameter param : original.getParameters())
		{
			try{
				List<FeedbackParameter> parameters = getParameters();
				Optional<FeedbackParameter> newParam = getParameters().stream().filter(p -> param.getId().equals(p.getId())).findFirst();
				
				if(!newParam.isPresent())
				{
					getParameters().add(param);
				}else{ 
					newParam.get().Merge(param);
				}
			} catch(NullPointerException e)
			{
				e.printStackTrace();
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
