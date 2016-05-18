package ch.uzh.ifi.feedback.orchestrator.interfaces;

import java.util.List;

import ch.uzh.ifi.feedback.orchestrator.FeedbackMechanism;

public interface ConfigurationProvider {
	List<FeedbackMechanism> GetFeedbackMechanisms(String application) throws Exception;
}
