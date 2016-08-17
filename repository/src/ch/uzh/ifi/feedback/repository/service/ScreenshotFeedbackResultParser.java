package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;


public class ScreenshotFeedbackResultParser extends DbResultParser<ScreenshotFeedback> {

	public ScreenshotFeedbackResultParser() {
		super(ScreenshotFeedback.class);
	}
}
