package ch.uzh.ifi.feedback.repository;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.RestManager;

class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
//		bind(FeedbackController.class).to(FeedbackController.class);
//		bind(FeedbackSerializationService.class).to(FeedbackSerializationService.class);
//		bind(ScreenshotFeedbackParser.class).to(ScreenshotFeedbackParser.class);
//		bind(FeedbackResultParser.class).to(FeedbackResultParser.class);
//		bind(FeedbackService.class).to(FeedbackService.class);
//		bind(RatingFeedbackResultParser.class).to(RatingFeedbackResultParser.class);
//		bind(RatingFeedbackService.class).to(RatingFeedbackService.class);
//		bind(ScreenshotFeedbackService.class).to(ScreenshotFeedbackService.class);
//		bind(ScreenshotFeedbackResultParser.class).to(ScreenshotFeedbackResultParser.class);
//		bind(AudioFeedbackResultParser.class).to(AudioFeedbackResultParser.class);
//		bind(AttachmentFeedbackResultParser.class).to(AttachmentFeedbackResultParser.class);
//		bind(AudioFeedbackParser.class).to(AudioFeedbackParser.class);
//		bind(AttachmentFeedbackParser.class).to(AttachmentFeedbackParser.class);
//		bind(FileStorageService.class).to(FileStorageService.class);
//		bind(AudioFeedbackService.class).to(AudioFeedbackService.class);
//		bind(AttachmentFeedbackService.class).to(AttachmentFeedbackService.class);
//		bind(CategoryResultParser.class).to(CategoryResultParser.class);
//		bind(CategoryFeedbackService.class).to(CategoryFeedbackService.class);
//		bind(TextAnnotationService.class).to(TextAnnotationService.class);
//		bind(TextAnnotationResultParser.class).to(TextAnnotationResultParser.class);
//		bind(ContextInformationResultParser.class).to(ContextInformationResultParser.class);
//		bind(ContextInformationService.class).to(ContextInformationService.class);
//		bind(AttachmentValidator.class).to(AttachmentValidator.class);
//		bind(AudioValidator.class).to(AudioValidator.class);
//		bind(CategoryValidator.class).to(CategoryValidator.class);
//		bind(ContextValidator.class).to(ContextValidator.class);
//		bind(FeedbackValidator.class).to(FeedbackValidator.class);
//		bind(RatingValidator.class).to(RatingValidator.class);
//		bind(ScreenshotValidator.class).to(ScreenshotValidator.class);
//		bind(TextAnnotationValidator.class).to(TextAnnotationValidator.class);
//		bind(TextFeedbackValidator.class).to(TextFeedbackValidator.class);
		bind(IRestManager.class).to(RestManager.class);
	}

}
