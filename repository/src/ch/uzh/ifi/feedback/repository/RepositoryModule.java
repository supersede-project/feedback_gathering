package ch.uzh.ifi.feedback.repository;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.controller.FeedbackController;
import ch.uzh.ifi.feedback.repository.serialization.FeedbackSerializationService;
import ch.uzh.ifi.feedback.repository.serialization.ScreenshotFeedbackParser;
import ch.uzh.ifi.feedback.repository.service.FeedbackResultParser;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.service.RatingFeedbackResultParser;
import ch.uzh.ifi.feedback.repository.service.RatingFeedbackService;
import ch.uzh.ifi.feedback.repository.service.ScreenshotFeedbackResultParser;
import ch.uzh.ifi.feedback.repository.service.ScreenshotFeedbackService;

public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FeedbackController.class).to(FeedbackController.class);
		bind(FeedbackSerializationService.class).to(FeedbackSerializationService.class);
		bind(ScreenshotFeedbackParser.class).to(ScreenshotFeedbackParser.class);
		bind(FeedbackResultParser.class).to(FeedbackResultParser.class);
		bind(FeedbackService.class).to(FeedbackService.class);
		bind(RatingFeedbackResultParser.class).to(RatingFeedbackResultParser.class);
		bind(RatingFeedbackService.class).to(RatingFeedbackService.class);
		bind(ScreenshotFeedbackService.class).to(ScreenshotFeedbackService.class);
		bind(ScreenshotFeedbackResultParser.class).to(ScreenshotFeedbackResultParser.class);
		bind(DatabaseConfiguration.class).to(DatabaseConfiguration.class).asEagerSingleton();
	}

}
