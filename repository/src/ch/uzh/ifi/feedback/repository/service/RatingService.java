package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.Rating;

public class RatingService extends ServiceBase<Rating> {
	@Inject
	public RatingService(RatingResultParser resultParser) {
		super(resultParser, Rating.class, "rating_feedbacks", "feedback_repository");
	}
}
