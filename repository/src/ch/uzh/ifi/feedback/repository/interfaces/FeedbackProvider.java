package ch.uzh.ifi.feedback.repository.interfaces;

import java.util.List;

import ch.uzh.ifi.feedback.repository.Feedback;

public interface FeedbackProvider {
	List<Feedback> GetFeedbacks(String application) throws Exception;
}
