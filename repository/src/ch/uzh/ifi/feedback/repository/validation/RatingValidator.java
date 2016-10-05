package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.service.RatingFeedbackService;

@Singleton
public class RatingValidator extends ValidatorBase<RatingFeedback> {

	@Inject
	public RatingValidator(RatingFeedbackService service, ValidationSerializer serializer) {
		super(RatingFeedback.class, service, serializer);
	}

}
