package ch.uzh.ifi.feedback.orchestrator;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.RestManager;
import ch.uzh.ifi.feedback.library.rest.authorization.IApiUserService;
import ch.uzh.ifi.feedback.orchestrator.services.ApiUserService;

public class OrchestratorModule extends AbstractModule{

	@Override
	protected void configure() {
		
	    bind(IApiUserService.class).to(ApiUserService.class);
	    bind(IRestManager.class).to(RestManager.class);
	}
}
