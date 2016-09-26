package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;

public class FeedbackValidator extends ValidatorBase<Feedback> {

	private AttachmentValidator attachmentValidator;
	private AudioValidator audioValidator;
	private CategoryValidator categoryValidator;
	private ContextValidator contextValidator;
	private RatingValidator ratingValidator;
	private ScreenshotValidator screenshotValidator;
	private TextFeedbackValidator textValidator;

	@Inject
	public FeedbackValidator(
			FeedbackService service,
			AttachmentValidator attachmentValidator,
			AudioValidator audioValidator,
			CategoryValidator categoryValidator,
			ContextValidator contextValidator,
			RatingValidator ratingValidator,
			ScreenshotValidator screenshotValidator,
			TextFeedbackValidator textValidator,
			ValidationSerializer serializer) {
		super(Feedback.class, service, serializer);
		
		this.attachmentValidator = attachmentValidator;
		this.audioValidator = audioValidator;
		this.categoryValidator = categoryValidator;
		this.contextValidator = contextValidator;
		this.ratingValidator = ratingValidator;
		this.screenshotValidator = screenshotValidator;
		this.textValidator = textValidator;
	}
	
	@Override
	public ValidationResult Validate(Feedback feedback) throws Exception {
		ValidationResult result =  super.Validate(feedback);
		
		for(TextFeedback text : feedback.getTextFeedbacks())
		{
			ValidateChild(result, textValidator, text);
		}
		
		for(RatingFeedback rating : feedback.getRatingFeedbacks())
		{
			ValidateChild(result, ratingValidator, rating);
		}
		
		for(ScreenshotFeedback screenshot : feedback.getScreenshotFeedbacks())
		{
			ValidateChild(result, screenshotValidator, screenshot);
		}
		
		for(AudioFeedback audio : feedback.getAudioFeedbacks())
		{
			ValidateChild(result, audioValidator, audio);
		}
		
		for(AttachmentFeedback attachment : feedback.getAttachmentFeedbacks())
		{
			ValidateChild(result, attachmentValidator, attachment);
		}
		
		for(CategoryFeedback category : feedback.getCategoryFeedbacks())
		{
			ValidateChild(result, categoryValidator, category);
		}
		
		if(feedback.getContextInformation() != null)
		{
			ValidateChild(result, contextValidator, feedback.getContextInformation());
		}
		
		return result;
	}
	
	private <T extends IDbItem> void  ValidateChild(ValidationResult result, ValidatorBase<T> validator, T value) throws Exception
	{
		ValidationResult childResult = validator.Validate(value);
		if(childResult.hasErrors())
		{
			result.setHasErrors(true);
			result.GetValidationErrors().addAll(childResult.GetValidationErrors());
		}
	}
}
