package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserPermission;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserPermissionResultParser;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.transaction.OrchestratorDatabaseConfiguration;

@Singleton
public class ApiUserPermissionService extends ServiceBase<ApiUserPermission>{

	@Inject
	public ApiUserPermissionService(
			ApiUserPermissionResultParser resultParser,
			OrchestratorDatabaseConfiguration config) 
	{
		super(resultParser, ApiUserPermission.class, "api_user_permissions", config.getDatabase());
	}

}
