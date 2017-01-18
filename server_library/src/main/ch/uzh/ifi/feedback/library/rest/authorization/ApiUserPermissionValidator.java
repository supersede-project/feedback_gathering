package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;

@Singleton
public class ApiUserPermissionValidator extends ValidatorBase<ApiUserPermission>{

	@Inject
	public ApiUserPermissionValidator(
			ApiUserPermissionService service,
			ValidationSerializer serializer) 
	{
		super(ApiUserPermission.class, service, serializer);

	}

}
