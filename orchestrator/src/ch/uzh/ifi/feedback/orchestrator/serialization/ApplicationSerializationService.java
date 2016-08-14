package ch.uzh.ifi.feedback.orchestrator.serialization;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.Application;

public class ApplicationSerializationService extends OrchestratorSerializationService<Application> 
{
	private GeneralConfigurationSerializationService generalConfigurationSerializationService;
	private ConfigurationSerializationService configurationSerializationService;
	
	@Inject
	public ApplicationSerializationService(
			GeneralConfigurationSerializationService generalConfigurationSerializationService,
			ConfigurationSerializationService configurationSerializationService)
	{
		this.configurationSerializationService = configurationSerializationService;
		this.generalConfigurationSerializationService = generalConfigurationSerializationService;
	}
	
	@Override
	public Application Deserialize(HttpServletRequest request) {
		Application app = super.Deserialize(request);
		SetNestedParameters(app);
		return app;
	}

	@Override
	public void SetNestedParameters(Application app) {
		app.getConfigurations().stream().forEach(c -> configurationSerializationService.SetNestedParameters(c));
		generalConfigurationSerializationService.SetNestedParameters(app.getGeneralConfiguration());
	}
}
