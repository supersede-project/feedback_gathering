package ch.uzh.ifi.feedback.orchestrator;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.orchestrator.controllers.MechanismController;
import ch.uzh.ifi.feedback.orchestrator.controllers.ParameterController;
import ch.uzh.ifi.feedback.orchestrator.serialization.MechanismSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.ParameterSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import ch.uzh.ifi.feedback.orchestrator.services.ParameterService;

public class OrchestratorModule extends AbstractModule{

	@Override
	protected void configure() {
	    bind(ParameterService.class).to(ParameterService.class);
	    bind(MechanismService.class).to(MechanismService.class);
	    bind(MechanismSerializationService.class).to(MechanismSerializationService.class);
	    bind(ParameterSerializationService.class).to(ParameterSerializationService.class);
	    bind(MechanismController.class).to(MechanismController.class);
	    bind(ParameterController.class).to(ParameterController.class);
	}

}
