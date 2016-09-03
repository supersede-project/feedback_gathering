package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;

public class ScreenshotFeedbackService extends ServiceBase<ScreenshotFeedback> {

	@Inject
	public ScreenshotFeedbackService(ScreenshotFeedbackResultParser resultParser, DatabaseConfiguration dbConfig) 
	{
		super(resultParser, ScreenshotFeedback.class, "screenshot_feedbacks", dbConfig.getRepositoryDb());
	}
}
