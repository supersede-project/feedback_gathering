package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.User;

public class UserService extends OrchestratorService<User>{

	@Inject
	public UserService(
			UserResultParser resultParser,  
			DatabaseConfiguration config) {
		super(resultParser, User.class, "users", config.getOrchestratorDb());
	}

}
