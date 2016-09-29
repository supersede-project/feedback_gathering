package ch.uzh.ifi.feedback.orchestrator.serialization;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;

public class GeneralConfigurationSerializationService extends OrchestratorSerializationService<GeneralConfiguration> 
{
	private ParameterSerializationService parameterSerializationService;
	
	@Inject
	public GeneralConfigurationSerializationService(ParameterSerializationService parameterSerializationService)
	{
		this.parameterSerializationService = parameterSerializationService;
	}
	
	@Override
	public GeneralConfiguration Deserialize(HttpServletRequest request) {
		GeneralConfiguration config = super.Deserialize(request);
		SetNestedParameters(config);
		return config;
	}
	
	public void SetNestedParameters(GeneralConfiguration config)
	{
		config.getParameters().stream().forEach(p -> parameterSerializationService.SetNestedParameters(p));
	}
}
