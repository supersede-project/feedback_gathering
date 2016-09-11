package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;

public class RatingFeedbackService extends ServiceBase<RatingFeedback> {

	@Inject
	public RatingFeedbackService(RatingFeedbackResultParser resultParser, DatabaseConfiguration dbConfig) {
		super(resultParser, RatingFeedback.class, "rating_feedbacks", dbConfig.getRepositoryDb());
	}
}
