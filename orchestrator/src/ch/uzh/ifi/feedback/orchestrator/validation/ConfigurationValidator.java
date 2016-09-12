package ch.uzh.ifi.feedback.orchestrator.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationError;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;

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
		
		List<Object> childrenErrors = new ArrayList<>();
		for(FeedbackMechanism mechanism : object.getFeedbackMechanisms())
		{
			ValidationResult childResult = mechanismValidator.Validate(mechanism);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				List<ValidationError> errors = childResult.GetValidationErrors();
				childrenErrors.add(errors);
			}
		}
		result.GetValidationErrors().add(new ValidationError("Mechanisms", childrenErrors));
		
		GeneralConfiguration config = object.getGeneralConfiguration();
		if(config != null)
		{
			ValidationResult childResult = generalConfigurationValidator.Validate(config);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				result.GetValidationErrors().add(new ValidationError("generalConfiguration", childResult.GetValidationErrors()));
			}
		}
		
		if(result.hasErrors())
			throw new ValidationException(serializer.Serialize(result));
		
		return result;
	}

}
