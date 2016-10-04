package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;

@Singleton
public class UserGroupResultParser extends DbResultParser<UserGroup> {

	public UserGroupResultParser() {
		super(UserGroup.class);
	}

}
