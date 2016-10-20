package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;

@Singleton
public class RatingFeedbackService extends ServiceBase<RatingFeedback> {

	@Inject
	public RatingFeedbackService(RatingFeedbackResultParser resultParser, IDatabaseConfiguration dbConfig) {
		super(resultParser, RatingFeedback.class, "rating_feedbacks", dbConfig.getDatabase());
	}
}
