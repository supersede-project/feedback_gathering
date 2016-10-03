package ch.uzh.ifi.feedback.orchestrator.validation;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.services.ParameterService;

@Singleton
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
			for(FeedbackParameter param : children)
			{
				ValidationResult childResult = Validate(param);
				if(childResult.hasErrors())
				{
					result.setHasErrors(true);
					result.GetValidationErrors().addAll(childResult.GetValidationErrors());
				}
			}
		}
		
		return result;
	}
}
