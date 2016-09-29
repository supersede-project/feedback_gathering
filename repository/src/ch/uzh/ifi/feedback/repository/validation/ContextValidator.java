package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.ContextInformation;
import ch.uzh.ifi.feedback.repository.service.ContextInformationService;

@Singleton
public class ContextValidator extends ValidatorBase<ContextInformation> {

	@Inject
	public ContextValidator(ContextInformationService service, ValidationSerializer serializer) {
		super(ContextInformation.class, service, serializer);
	}

}
