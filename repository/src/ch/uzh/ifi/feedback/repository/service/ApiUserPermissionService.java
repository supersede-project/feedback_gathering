package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserPermission;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserPermissionResultParser;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;

@Singleton
public class ApiUserPermissionService extends ServiceBase<ApiUserPermission>{

	@Inject
	public ApiUserPermissionService(
			ApiUserPermissionResultParser resultParser,
			DatabaseConfiguration config) 
	{
		super(resultParser, ApiUserPermission.class, "api_user_permissions", config.getRepositoryDb());
		// TODO Auto-generated constructor stub
	}

}
