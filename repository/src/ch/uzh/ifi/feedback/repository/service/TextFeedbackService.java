package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;

public class TextFeedbackService extends ServiceBase<TextFeedback> {

	@Inject
	public TextFeedbackService(TextFeedbackResultParser resultParser) {
		super(resultParser, TextFeedback.class, "text_feedbacks", "feedback_repository");
	}
}
