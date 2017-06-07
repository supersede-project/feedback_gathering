package ch.uzh.ifi.feedback.orchestrator.serialization;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;

@Singleton
public class MechanismSerializationService extends OrchestratorSerializationService<FeedbackMechanism> 
{
	private ParameterSerializationService parameterSerializationService;
	
	@Inject
	public MechanismSerializationService(ParameterSerializationService parameterSerializationService)
	{
		this.parameterSerializationService = parameterSerializationService;
	}
	
	@Override
	public FeedbackMechanism Deserialize(HttpServletRequest request) {
		FeedbackMechanism mechanism = super.Deserialize(request);
		
		return mechanism;
	}
	
	@Override
	public void SetNestedParameters(FeedbackMechanism mechanism) {
		mechanism.getParameters().stream().forEach(p -> parameterSerializationService.SetNestedParameters(p));
	}
}
