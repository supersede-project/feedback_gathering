package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserResultParser;
import ch.uzh.ifi.feedback.library.rest.authorization.IApiUserService;

@Singleton
public class ApiUserService extends ServiceBase<ApiUser> implements IApiUserService {

	@Inject
	public ApiUserService(
			ApiUserResultParser resultParser, 
			DatabaseConfiguration config) {
		super(resultParser, ApiUser.class, "api_users", config.getRepositoryDb());
	}

}
