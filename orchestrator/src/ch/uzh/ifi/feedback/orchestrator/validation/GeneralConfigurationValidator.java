package ch.uzh.ifi.feedback.orchestrator.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationError;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.GeneralConfigurationService;

public class GeneralConfigurationValidator extends ValidatorBase<GeneralConfiguration>{

	private ParameterValidator parameterValidator;
	
	@Inject
	public GeneralConfigurationValidator(ParameterValidator parameterValidator, GeneralConfigurationService service) {
		super(GeneralConfiguration.class, service);
		this.parameterValidator = parameterValidator;
	}
	
	@Override
	public ValidationResult Validate(GeneralConfiguration object) throws Exception {
		ValidationResult result = super.Validate(object);
		
		List<Object> childrenErrors = new ArrayList<>();
		for(FeedbackParameter param : object.getParameters())
		{
			ValidationResult childResult = parameterValidator.Validate(param);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				List<ValidationError> errors = childResult.GetValidationErrors();
				childrenErrors.add(errors);
			}
		}
		result.GetValidationErrors().add(new ValidationError("Parameters", childrenErrors));
		return result;
		/*
		for(FeedbackParameter param : object.getParameters())
		{
			parameterValidator.Validate(param, result);
		}*/
	}

}
