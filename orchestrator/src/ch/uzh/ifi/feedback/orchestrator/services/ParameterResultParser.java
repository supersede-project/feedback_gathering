package ch.uzh.ifi.feedback.orchestrator.services;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;

public class ParameterResultParser extends DbResultParser<FeedbackParameter> {

	public ParameterResultParser() {
		super(FeedbackParameter.class);
	}

}
