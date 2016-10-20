package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Timestamp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.User;

@Singleton
public class UserService extends OrchestratorService<User>{

	@Inject
	public UserService(
			UserResultParser resultParser,  
			IDatabaseConfiguration config,
			@Named("timestamp")Provider<Timestamp> timestampProvider) 
	{
		super(resultParser, User.class, "users", config.getDatabase(), timestampProvider);
	}

}
