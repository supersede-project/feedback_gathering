package ch.uzh.ifi.feedback.orchestrator.services;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.User;

public class UserResultParser extends DbResultParser<User> {
	public UserResultParser() {
		super(User.class);
	}
}
