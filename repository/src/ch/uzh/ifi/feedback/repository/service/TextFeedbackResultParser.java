package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;

@Singleton
public class TextFeedbackResultParser extends DbResultParser<TextFeedback> {

	public TextFeedbackResultParser() {
		super(TextFeedback.class);
	}
}
