package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;
import ch.uzh.ifi.feedback.repository.service.TextFeedbackService;

public class TextFeedbackValidator extends ValidatorBase<TextFeedback> {

	@Inject
	public TextFeedbackValidator(TextFeedbackService service, ValidationSerializer serializer) {
		super(TextFeedback.class, service, serializer);
	}

}
