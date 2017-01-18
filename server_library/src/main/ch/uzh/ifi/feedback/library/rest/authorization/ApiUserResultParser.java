package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;
import ch.uzh.ifi.feedback.library.transaction.DbResultParser;

@Singleton
public class ApiUserResultParser extends DbResultParser<ApiUser> {

	public ApiUserResultParser() {
		super(ApiUser.class);
	}

}
