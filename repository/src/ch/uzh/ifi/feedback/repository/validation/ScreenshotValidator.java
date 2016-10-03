package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.TextAnnotation;
import ch.uzh.ifi.feedback.repository.service.ScreenshotFeedbackService;

@Singleton
public class ScreenshotValidator extends ValidatorBase<ScreenshotFeedback>{

	private TextAnnotationValidator annotationValidator;

	@Inject
	public ScreenshotValidator(
			ScreenshotFeedbackService service,
			TextAnnotationValidator annotationValidator,
			ValidationSerializer serializer) {
		super(ScreenshotFeedback.class, service, serializer);
		this.annotationValidator = annotationValidator;
	}
	
	@Override
	public ValidationResult Validate(ScreenshotFeedback object) throws Exception {
		ValidationResult result =  super.Validate(object);
		
		for(TextAnnotation annotation : object.getTextAnnotations())
		{
			ValidationResult childResult = annotationValidator.Validate(annotation);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				result.GetValidationErrors().addAll(childResult.GetValidationErrors());
			}
		}
		
		return result;
	}

}
