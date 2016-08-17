package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;

public class RatingService extends ServiceBase<RatingFeedback> {
	@Inject
	public RatingService(RatingResultParser resultParser) {
		super(resultParser, RatingFeedback.class, "rating_feedbacks", "feedback_repository");
	}
}
