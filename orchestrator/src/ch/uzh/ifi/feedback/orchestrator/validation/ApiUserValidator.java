package ch.uzh.ifi.feedback.orchestrator.validation;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.ApiUser;
import ch.uzh.ifi.feedback.orchestrator.services.ApiUserService;

public class ApiUserValidator extends ValidatorBase<ApiUser>{

	@Inject
	public ApiUserValidator(ApiUserService service, ValidationSerializer serializer) {
		super(ApiUser.class, service, serializer);
	}
}
