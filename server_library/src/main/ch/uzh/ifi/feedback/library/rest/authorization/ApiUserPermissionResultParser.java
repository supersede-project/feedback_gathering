package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;

@Singleton
public class ApiUserPermissionResultParser extends DbResultParser<ApiUserPermission>{

	public ApiUserPermissionResultParser() {
		super(ApiUserPermission.class);
	}
}
