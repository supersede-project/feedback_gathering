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
	public Feedback GetById(int id) throws SQLException, NotFoundException {
		Feedback feedback =  super.GetById(id);
		feedback.setRatings(ratingService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setScreenshots(screenshotService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		return feedback;
	}
	
	@Override
	public List<Feedback> GetAll() throws SQLException, NotFoundException {
		List<Feedback> feedbacks = super.GetAll();
		for(Feedback f : feedbacks)
		{
			f.setRatings(ratingService.GetWhere(Arrays.asList(f.getId()), "feedback_id = ?"));
			f.setScreenshots(screenshotService.GetWhere(Arrays.asList(f.getId()), "feedback_id = ?"));
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

}
