package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.User;

@Singleton
public class UserResultParser extends DbResultParser<User> {
	public UserResultParser() {
		super(User.class);
	}
}
