package ch.uzh.ifi.feedback.orchestrator.serialization;


import ch.uzh.ifi.feedback.library.rest.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;

public class ParameterSerializationService extends DefaultSerializer<FeedbackParameter>{

	public ParameterSerializationService() {
		super(FeedbackParameter.class);
		// TODO Auto-generated constructor stub
	}

}
