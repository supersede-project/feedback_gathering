package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;

@Singleton
public class TextFeedbackService extends ServiceBase<TextFeedback> {

	@Inject
	public TextFeedbackService(TextFeedbackResultParser resultParser, IDatabaseConfiguration dbConfig) 
	{
		super(resultParser, TextFeedback.class, "text_feedbacks", dbConfig.getDatabase());
	}
}
