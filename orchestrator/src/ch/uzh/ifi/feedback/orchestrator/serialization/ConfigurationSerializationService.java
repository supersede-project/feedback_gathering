package ch.uzh.ifi.feedback.orchestrator.serialization;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;

public class ConfigurationSerializationService extends OrchestratorSerializationService<Configuration>
{
	private MechanismSerializationService mechanismSerializationService;
	private GeneralConfigurationSerializationService generalConfigurationSerializationService;
	
	@Inject
	public ConfigurationSerializationService(
			MechanismSerializationService mechanismSerializationService,
			GeneralConfigurationSerializationService generalConfigurationSerializationService)
	{
		this.mechanismSerializationService = mechanismSerializationService;
		this.generalConfigurationSerializationService = generalConfigurationSerializationService;
	}
	
	@Override
	public Configuration Deserialize(HttpServletRequest request) {

		Configuration config =  super.Deserialize(request);
		SetNestedParameters(config);
		return config;
	}
	
	@Override
	public void SetNestedParameters(Configuration config) {
		config.getFeedbackMechanisms().forEach(m -> mechanismSerializationService.SetNestedParameters(m));
		generalConfigurationSerializationService.SetNestedParameters(config.getGeneralConfiguration());
	}
	
}
