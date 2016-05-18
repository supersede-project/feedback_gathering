package ch.uzh.ifi.feedback.repository.interfaces;

import ch.uzh.ifi.feedback.repository.Feedback;

public interface FeedbackReceiver {
	void StoreFeedback(Feedback feedback) throws Exception;
}
