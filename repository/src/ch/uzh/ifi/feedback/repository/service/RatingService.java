package ch.uzh.ifi.feedback.repository.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.Rating;
import javassist.NotFoundException;

public class RatingService extends ServiceBase<Rating> {

	@Inject
	public RatingService(RatingResultParser resultParser) {
		super(resultParser, Rating.class, "rating_feedbacks", "feedback_repository");
	}
	
	@Override
	public List<Rating> GetAllFor(Connection con, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException {
		return super.GetAllFor(con, "feedbacks", "feedback_id", foreignKey);
	}
}
