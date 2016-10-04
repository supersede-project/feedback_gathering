package ch.uzh.ifi.feedback.orchestrator.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;
import ch.uzh.ifi.feedback.orchestrator.services.UserGroupService;

@Singleton
public class UserGroupValidator extends ValidatorBase<UserGroup> {

	@Inject
	public UserGroupValidator(UserGroupService service, ValidationSerializer serializer) {
		super(UserGroup.class, service, serializer);
	}
}
