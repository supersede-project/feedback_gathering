package ch.uzh.ifi.feedback.orchestrator.validation;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;

public class UserGroupValidator extends ValidatorBase<UserGroup> {

	@Inject
	public UserGroupValidator(ServiceBase<UserGroup> service, ValidationSerializer serializer) {
		super(UserGroup.class, service, serializer);
	}
}
