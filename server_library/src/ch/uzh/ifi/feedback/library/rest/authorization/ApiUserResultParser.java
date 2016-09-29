package ch.uzh.ifi.feedback.library.rest.authorization;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;

public class ApiUserResultParser extends DbResultParser<ApiUser> {

	public ApiUserResultParser() {
		super(ApiUser.class);
	}

}
