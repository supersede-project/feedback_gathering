package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;

@Singleton
public class ScreenshotFeedbackResultParser extends DbResultParser<ScreenshotFeedback> {

	public ScreenshotFeedbackResultParser() {
		super(ScreenshotFeedback.class);
	}
}
