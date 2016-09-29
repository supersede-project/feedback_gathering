package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.service.RatingFeedbackService;

public class RatingValidator extends ValidatorBase<RatingFeedback> {

	@Inject
	public RatingValidator(RatingFeedbackService service, ValidationSerializer serializer) {
		super(RatingFeedback.class, service, serializer);
	}

}
