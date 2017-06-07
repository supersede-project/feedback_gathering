package ch.uzh.ifi.feedback.orchestrator.serialization;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;

@Singleton
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
		config.getParameters().stream().forEach(p -> 
		{
			if(p != null)
				parameterSerializationService.SetNestedParameters(p);
		});
	}
}
