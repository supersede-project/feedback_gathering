package ch.uzh.ifi.feedback.repository;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.repository.controller.FeedbackController;
import ch.uzh.ifi.feedback.repository.serialization.FeedbackSerializationService;
import ch.uzh.ifi.feedback.repository.serialization.ScreenshotSerializationService;
import ch.uzh.ifi.feedback.repository.service.FeedbackResultParser;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.service.RatingResultParser;
import ch.uzh.ifi.feedback.repository.service.RatingFeedbackService;
import ch.uzh.ifi.feedback.repository.service.ScreenshotResultParser;
import ch.uzh.ifi.feedback.repository.service.ScreenshotFeedbackService;

public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FeedbackController.class).to(FeedbackController.class);
		bind(FeedbackSerializationService.class).to(FeedbackSerializationService.class);
		bind(ScreenshotSerializationService.class).to(ScreenshotSerializationService.class);
		bind(FeedbackResultParser.class).to(FeedbackResultParser.class);
		bind(FeedbackService.class).to(FeedbackService.class);
		bind(RatingResultParser.class).to(RatingResultParser.class);
		bind(RatingFeedbackService.class).to(RatingFeedbackService.class);
		bind(ScreenshotFeedbackService.class).to(ScreenshotFeedbackService.class);
		bind(ScreenshotResultParser.class).to(ScreenshotResultParser.class);
	}

}
