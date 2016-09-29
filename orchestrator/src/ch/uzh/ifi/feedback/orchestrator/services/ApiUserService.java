package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.orchestrator.model.ApiUser;

public class ApiUserService extends ServiceBase<ApiUser> {

	@Inject
	public ApiUserService(
			ApiUserResultParser resultParser, 
			DatabaseConfiguration config) 
	{
		super(resultParser, ApiUser.class, "api_users", config.getOrchestratorDb());
	}

}
