package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.service.CategoryFeedbackService;

public class CategoryValidator extends ValidatorBase<CategoryFeedback> {

	@Inject
	public CategoryValidator(CategoryFeedbackService service, ValidationSerializer serializer) {
		super(CategoryFeedback.class, service, serializer);
	}

}
