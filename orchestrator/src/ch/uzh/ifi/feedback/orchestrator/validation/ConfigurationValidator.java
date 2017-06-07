package ch.uzh.ifi.feedback.orchestrator.validation;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationError;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.ConfigurationType;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import static java.util.Arrays.asList;

@Singleton
public class ConfigurationValidator extends ValidatorBase<Configuration> {

	private MechanismValidator mechanismValidator;
	private GeneralConfigurationValidator generalConfigurationValidator;
	private ConfigurationService configurationService;
	
	@Inject
	public ConfigurationValidator(
			MechanismValidator mechanismValidator, 
			GeneralConfigurationValidator generalConfigurationValidator,
			ConfigurationService service,
			ValidationSerializer serializer) 
	{
		super(Configuration.class, service, serializer);
		
		this.mechanismValidator = mechanismValidator;
		this.generalConfigurationValidator = generalConfigurationValidator;
		this.configurationService = service;
	}
	
	@Override
	public ValidationResult Validate(Configuration object) throws Exception {
		ValidationResult result = super.Validate(object);
		
		//check that no more than 1 push configuration is present per user group and application
		if(object.getType().equals(ConfigurationType.PUSH))
		{
			List<Configuration> configs = configurationService.GetWhere(
					asList(object.getUserGroupsId(), object.getApplicationId(), object.getType().toString()),
					"user_groups_id = ?", "applications_id = ?", "type = ?");
			
			if(configs.size() == 1)
			{
				Configuration other = configs.get(0);
				if(object.getId() == null || !other.getId().equals(object.getId()))
				{
					result.setHasErrors(true);
					ValidationError error = new ValidationError("type", object.getType(), "unique: there can only be one push configuration per user group and application");
					result.GetValidationErrors().add(error);
				}
			}
		}
		
		for(FeedbackMechanism mechanism : object.getFeedbackMechanisms())
		{
			ValidationResult childResult = mechanismValidator.Validate(mechanism);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				result.GetValidationErrors().addAll(childResult.GetValidationErrors());
			}
		}

		GeneralConfiguration config = object.getGeneralConfiguration();
		if(config != null)
		{
			ValidationResult childResult = generalConfigurationValidator.Validate(config);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				result.GetValidationErrors().addAll(childResult.GetValidationErrors());
			}
		}
		
		return result;
	}

}
