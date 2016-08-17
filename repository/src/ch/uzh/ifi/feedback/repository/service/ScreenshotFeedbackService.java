package ch.uzh.ifi.feedback.repository.service;
import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;


public class ScreenshotFeedbackService extends ServiceBase<ScreenshotFeedback> {

	@Inject
	public ScreenshotFeedbackService(ScreenshotResultParser resultParser) 
	{
		super(resultParser, ScreenshotFeedback.class, "screenshot_feedbacks", "feedback_repository");
	}

}
