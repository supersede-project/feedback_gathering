package ch.uzh.ifi.feedback.orchestrator.serialization;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;

public class MechanismSerializationService extends DefaultSerializer<FeedbackMechanism> 
{
	private ParameterSerializationService parameterSerializationService;
	
	@Inject
	public MechanismSerializationService(ParameterSerializationService parameterSerializationService)
	{
		this.parameterSerializationService = parameterSerializationService;
	}
	
	@Override
	public FeedbackMechanism Deserialize(String data) {
		FeedbackMechanism mechanism = super.Deserialize(data);
		
		return mechanism;
	}
	
	@Override
	public void SetNestedParameters(FeedbackMechanism mechanism) {
		mechanism.getParameters().stream().forEach(p -> parameterSerializationService.SetNestedParameters(p));
	}
}
