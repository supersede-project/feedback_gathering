package ch.uzh.ifi.feedback.orchestrator;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.controllers.ApplicationController;
import ch.uzh.ifi.feedback.orchestrator.controllers.ConfigurationController;
import ch.uzh.ifi.feedback.orchestrator.controllers.MechanismController;
import ch.uzh.ifi.feedback.orchestrator.controllers.ParameterController;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.ConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.MechanismSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.ParameterSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationResultParser;
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationService;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationResultParser;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.GeneralConfigurationResultParser;
import ch.uzh.ifi.feedback.orchestrator.services.GeneralConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismResultParser;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import ch.uzh.ifi.feedback.orchestrator.services.ParameterResultParser;
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
	    bind(MechanismResultParser.class).to(MechanismResultParser.class);
	    bind(ParameterResultParser.class).to(ParameterResultParser.class);
	    bind(GeneralConfigurationResultParser.class).to(GeneralConfigurationResultParser.class);
	    bind(GeneralConfigurationService.class).to(GeneralConfigurationService.class);
	    bind(ConfigurationSerializationService.class).to(ConfigurationSerializationService.class);
	    bind(ConfigurationResultParser.class).to(ConfigurationResultParser.class);
	    bind(ConfigurationService.class).to(ConfigurationService.class);
	    bind(ConfigurationController.class).to(ConfigurationController.class);
	    bind(ApplicationSerializationService.class).to(ApplicationSerializationService.class);
	    bind(ApplicationResultParser.class).to(ApplicationResultParser.class);
	    bind(ApplicationService.class).to(ApplicationService.class);
	    bind(ApplicationController.class).to(ApplicationController.class);
	}

}
