package ch.uzh.ifi.feedback.orchestrator.serialization;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.Application;

public class ApplicationSerializationService extends DefaultSerializer<Application> 
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
	public Application Deserialize(String data) {
		Application app = super.Deserialize(data);
		SetNestedParameters(app);
		return app;
	}

	@Override
	public void SetNestedParameters(Application app) {
		app.getConfigurations().stream().forEach(c -> configurationSerializationService.SetNestedParameters(c));
		generalConfigurationSerializationService.SetNestedParameters(app.getGeneralConfiguration());
	}
}
