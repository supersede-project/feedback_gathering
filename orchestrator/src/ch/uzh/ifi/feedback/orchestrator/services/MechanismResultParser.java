package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;

@Singleton
public class MechanismResultParser extends DbResultParser<FeedbackMechanism> {

	public MechanismResultParser() {
		super(FeedbackMechanism.class);
	}

}
