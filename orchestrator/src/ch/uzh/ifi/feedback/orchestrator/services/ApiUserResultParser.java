package ch.uzh.ifi.feedback.orchestrator.services;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.ApiUser;

public class ApiUserResultParser extends DbResultParser<ApiUser> {

	public ApiUserResultParser() {
		super(ApiUser.class);
	}

}
