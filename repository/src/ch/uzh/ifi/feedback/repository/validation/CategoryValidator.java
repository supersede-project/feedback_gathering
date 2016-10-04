package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.service.CategoryFeedbackService;

@Singleton
public class CategoryValidator extends ValidatorBase<CategoryFeedback> {

	@Inject
	public CategoryValidator(CategoryFeedbackService service, ValidationSerializer serializer) {
		super(CategoryFeedback.class, service, serializer);
	}

}
