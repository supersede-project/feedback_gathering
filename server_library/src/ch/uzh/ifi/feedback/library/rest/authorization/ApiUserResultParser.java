package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;

@Singleton
public class ApiUserResultParser extends DbResultParser<ApiUser> {

	public ApiUserResultParser() {
		super(ApiUser.class);
	}

}
