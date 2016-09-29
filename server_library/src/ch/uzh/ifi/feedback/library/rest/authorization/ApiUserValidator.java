package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;

public class ApiUserValidator extends ValidatorBase<ApiUser>{

	@Inject
	public ApiUserValidator(IApiUserService service, ValidationSerializer serializer) {
		super(ApiUser.class, service, serializer);
	}
}
