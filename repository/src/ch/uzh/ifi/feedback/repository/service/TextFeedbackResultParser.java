package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;


public class TextFeedbackResultParser extends DbResultParser<TextFeedback> {

	public TextFeedbackResultParser() {
		super(TextFeedback.class);
	}
}
