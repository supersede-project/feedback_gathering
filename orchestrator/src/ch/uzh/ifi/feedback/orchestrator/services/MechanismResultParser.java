package ch.uzh.ifi.feedback.orchestrator.services;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;

public class MechanismResultParser extends DbResultParser<FeedbackMechanism> {

	public MechanismResultParser() {
		super(FeedbackMechanism.class);
	}

}
