package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;
import ch.uzh.ifi.feedback.repository.service.AudioFeedbackService;

public class AudioValidator extends ValidatorBase<AudioFeedback> {

	@Inject
	public AudioValidator(AudioFeedbackService service, ValidationSerializer serializer) {
		super(AudioFeedback.class, service, serializer);
	}

}
