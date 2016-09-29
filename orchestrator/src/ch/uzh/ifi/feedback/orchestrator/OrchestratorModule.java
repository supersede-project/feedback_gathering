package ch.uzh.ifi.feedback.orchestrator;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.RestManager;

public class OrchestratorModule extends AbstractModule{

	@Override
	protected void configure() {
		
	    bind(IRestManager.class).to(RestManager.class);
	}

}
