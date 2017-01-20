package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;

@Singleton
public class ApiUserValidator extends ValidatorBase<ApiUser>{

	@Inject
	public ApiUserValidator(ApiUserService service, ValidationSerializer serializer) {
		super(ApiUser.class, service, serializer);
	}
}
