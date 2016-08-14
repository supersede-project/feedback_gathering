package ch.uzh.ifi.feedback.repository.service;
import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.Screenshot;

public class ScreenshotService extends ServiceBase<Screenshot> {

	@Inject
	public ScreenshotService(ScreenshotResultParser resultParser) 
	{
		super(resultParser, Screenshot.class, "screenshot_feedbacks", "feedback_repository");
	}

}
