package ch.uzh.ifi.feedback.repository;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.controller.FeedbackController;
import ch.uzh.ifi.feedback.repository.serialization.AttachmentFeedbackParser;
import ch.uzh.ifi.feedback.repository.serialization.AudioFeedbackParser;
import ch.uzh.ifi.feedback.repository.serialization.FeedbackSerializationService;
import ch.uzh.ifi.feedback.repository.serialization.FileStorageService;
import ch.uzh.ifi.feedback.repository.serialization.ScreenshotFeedbackParser;
import ch.uzh.ifi.feedback.repository.service.AttachmentFeedbackResultParser;
import ch.uzh.ifi.feedback.repository.service.AttachmentFeedbackService;
import ch.uzh.ifi.feedback.repository.service.AudioFeedbackResultParser;
import ch.uzh.ifi.feedback.repository.service.AudioFeedbackService;
import ch.uzh.ifi.feedback.repository.service.CategoryFeedbackService;
import ch.uzh.ifi.feedback.repository.service.CategoryResultParser;
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
		bind(AudioFeedbackResultParser.class).to(AudioFeedbackResultParser.class);
		bind(AttachmentFeedbackResultParser.class).to(AttachmentFeedbackResultParser.class);
		bind(AudioFeedbackParser.class).to(AudioFeedbackParser.class);
		bind(AttachmentFeedbackParser.class).to(AttachmentFeedbackParser.class);
		bind(FileStorageService.class).to(FileStorageService.class);
		bind(AudioFeedbackService.class).to(AudioFeedbackService.class);
		bind(AttachmentFeedbackService.class).to(AttachmentFeedbackService.class);
		bind(CategoryResultParser.class).to(CategoryResultParser.class);
		bind(CategoryFeedbackService.class).to(CategoryFeedbackService.class);
	}

}
