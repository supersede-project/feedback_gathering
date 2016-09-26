package ch.uzh.ifi.feedback.orchestrator.services;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;

public class UserGroupResultParser extends DbResultParser<UserGroup> {

	public UserGroupResultParser() {
		super(UserGroup.class);
	}

}
