package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.TextAnnotation;
import ch.uzh.ifi.feedback.repository.service.TextAnnotationService;

@Singleton
public class TextAnnotationValidator extends ValidatorBase<TextAnnotation>{

	@Inject
	public TextAnnotationValidator(
			TextAnnotationService service,
			ValidationSerializer serializer) 
	{
		super(TextAnnotation.class, service, serializer);
	}

}
