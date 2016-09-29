package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;
import ch.uzh.ifi.feedback.repository.service.AttachmentFeedbackService;

@Singleton
public class AttachmentValidator extends ValidatorBase<AttachmentFeedback>{

	@Inject
	public AttachmentValidator(
			AttachmentFeedbackService service,
			ValidationSerializer serializer) {
		super(AttachmentFeedback.class, service, serializer);
	}

}
