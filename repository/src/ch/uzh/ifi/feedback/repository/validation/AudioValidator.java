package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;
import ch.uzh.ifi.feedback.repository.service.AudioFeedbackService;

@Singleton
public class AudioValidator extends ValidatorBase<AudioFeedback> {

	@Inject
	public AudioValidator(AudioFeedbackService service, ValidationSerializer serializer) {
		super(AudioFeedback.class, service, serializer);
	}

}
