package ch.uzh.ifi.feedback.repository.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.Rating;
import ch.uzh.ifi.feedback.repository.model.Screenshot;
import javassist.NotFoundException;

public class FeedbackService extends ServiceBase<Feedback>{

	private RatingService ratingService;
	private ScreenshotService screenshotService;
	
	@Inject
	public FeedbackService(
			FeedbackResultParser resultParser, 
			RatingService ratingService,
			ScreenshotService screenshotService) 
	{
		super(resultParser, Feedback.class, "feedbacks", "feedback_repository", ratingService, screenshotService);
		this.screenshotService = screenshotService;
		this.ratingService = ratingService;
	}

	@Override
	public Feedback GetById(Connection con, int id) throws SQLException, NotFoundException {
		Feedback feedback =  super.GetById(con, id);
		feedback.setRatings(ratingService.GetAllFor(con, "feedback_id", id));
		return feedback;
	}
	
	@Override
	public List<Feedback> GetAll(Connection con) throws SQLException, NotFoundException {
		List<Feedback> feedbacks = super.GetAll(con);
		for(Feedback f : feedbacks)
		{
			f.setRatings(ratingService.GetAllFor(con, "feedback_id", f.getId()));
		}

		return feedbacks;
	}
	
	@Override
	public int Insert(Connection con, Feedback feedback)
			throws SQLException, NotFoundException, UnsupportedOperationException
	{
		int feedbackId = super.Insert(con, feedback);
		for(Rating r : feedback.getRatings())
		{
			r.setFeedbackId(feedbackId);
			ratingService.Insert(con, r);
		}
		
		for(Screenshot s : feedback.getScreenshots())
		{
			s.setFeedbackId(feedbackId);
			screenshotService.Insert(con, s);
		}
		
		return feedbackId;
	}
	
	@Override
	public List<Feedback> GetAllFor(Connection con, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException {
		return super.GetWhereEquals(con, Arrays.asList("application_id"), Arrays.asList(foreignKey));
	}
}
