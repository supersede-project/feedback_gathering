package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.Feedback;

@Singleton
public class FeedbackResultParser extends DbResultParser<Feedback>{

	public FeedbackResultParser() {
		super(Feedback.class);
	}
}
