package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;

@Singleton
public class RatingFeedbackResultParser extends DbResultParser<RatingFeedback> {

	public RatingFeedbackResultParser() {
		super(RatingFeedback.class);
	}
}
