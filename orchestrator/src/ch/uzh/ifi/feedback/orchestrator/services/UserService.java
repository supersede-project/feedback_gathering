package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Timestamp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.User;

@Singleton
public class UserService extends OrchestratorService<User>{

	@Inject
	public UserService(
			UserResultParser resultParser,  
			DatabaseConfiguration config,
			@Named("timestamp")Provider<Timestamp> timestampProvider) 
	{
		super(resultParser, User.class, "users", config.getOrchestratorDb(), timestampProvider);
	}

}
