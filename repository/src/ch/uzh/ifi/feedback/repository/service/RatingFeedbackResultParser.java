package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;


public class RatingFeedbackResultParser extends DbResultParser<RatingFeedback> {

	public RatingFeedbackResultParser() {
		super(RatingFeedback.class);
	}
}
