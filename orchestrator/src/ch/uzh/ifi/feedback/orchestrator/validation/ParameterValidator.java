package ch.uzh.ifi.feedback.orchestrator.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationError;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.services.ParameterService;

public class ParameterValidator extends ValidatorBase<FeedbackParameter> {

	@Inject
	public ParameterValidator(ParameterService service, ValidationSerializer serializer) {
		super(FeedbackParameter.class, service, serializer);
	}
	
	@Override
	public ValidationResult Validate(FeedbackParameter object) throws Exception {
		ValidationResult result = super.Validate(object);
		
		if(object.getValue() instanceof List)
		{
			List<FeedbackParameter> children = (List<FeedbackParameter>)object.getValue();
			List<Object> childrenErrors = new ArrayList<>();
			for(FeedbackParameter param : children)
			{
				ValidationResult childResult = Validate(param);
				if(childResult.hasErrors())
				{
					result.setHasErrors(true);
					List<ValidationError> errors = childResult.GetValidationErrors();
					childrenErrors.add(errors);
				}
			}
			result.GetValidationErrors().add(new ValidationError("value", childrenErrors));
		}
		
		if(result.hasErrors())
			throw new ValidationException(serializer.Serialize(result));
		
		return result;
	}
}
