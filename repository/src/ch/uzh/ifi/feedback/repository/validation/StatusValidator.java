package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.Status;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.service.StatusService;

@Singleton
public class StatusValidator extends ValidatorBase<Status> {

	@Inject
	public StatusValidator(
			StatusService service,
			ValidationSerializer serializer) {
		super(Status.class, service, serializer);
		
	}
	
	@Override
	public ValidationResult Validate(Status status) throws Exception {
		ValidationResult result =  super.Validate(status);
		
		return result;
	}
	
	private <T extends IDbItem> void ValidateChild(ValidationResult result, ValidatorBase<T> validator, T value) throws Exception
	{
		ValidationResult childResult = validator.Validate(value);
		if(childResult.hasErrors())
		{
			result.setHasErrors(true);
			result.GetValidationErrors().addAll(childResult.GetValidationErrors());
		}
	}
}
