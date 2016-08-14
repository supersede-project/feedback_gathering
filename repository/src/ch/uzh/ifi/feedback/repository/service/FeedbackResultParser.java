package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.Feedback;

public class FeedbackResultParser extends DbResultParser<Feedback>{

	public FeedbackResultParser() {
		super(Feedback.class);
	}

}
