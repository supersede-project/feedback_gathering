package ch.uzh.ifi.feedback.repository.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;
import javassist.NotFoundException;

public class FeedbackService extends ServiceBase<Feedback> {

	private TextFeedbackService textFeedbackService;
	private RatingFeedbackService ratingFeedbackService;
	private ScreenshotFeedbackService screenshotFeedbackService;
	
	@Inject
	public FeedbackService(FeedbackResultParser resultParser, RatingFeedbackService ratingFeedbackService,
			ScreenshotFeedbackService screenshotFeedbackService, TextFeedbackService textFeedbackService) {
		super(resultParser, Feedback.class, "feedbacks", "feedback_repository", ratingFeedbackService, screenshotFeedbackService);
		this.screenshotFeedbackService = screenshotFeedbackService;
		this.ratingFeedbackService = ratingFeedbackService;
		this.textFeedbackService = textFeedbackService;
	}

	@Override
	public Feedback GetById(int id) throws SQLException, NotFoundException {
		Feedback feedback = super.GetById(id);
		setFeedbackRelations(feedback);

		return feedback;
	}

	@Override
	public List<Feedback> GetAll() throws SQLException, NotFoundException {
		List<Feedback> feedbacks = super.GetAll();

		for (Feedback feedback : feedbacks) {
			setFeedbackRelations(feedback);
		}

		return feedbacks;
	}

	@Override
	public int Insert(Connection con, Feedback feedback) throws SQLException, NotFoundException, UnsupportedOperationException {
		int feedbackId = super.Insert(con, feedback);
		
		if(feedback.getTextFeedbacks() != null) {
			for (TextFeedback textFeedback : feedback.getTextFeedbacks()) {
				textFeedback.setFeedbackId(feedbackId);
				textFeedbackService.Insert(con, textFeedback);
			}	
		}
	
		if(feedback.getRatingFeedbacks() != null) {
			for (RatingFeedback ratingFeedback : feedback.getRatingFeedbacks()) {
				ratingFeedback.setFeedbackId(feedbackId);
				ratingFeedbackService.Insert(con, ratingFeedback);
			}
		}
		
		if(feedback.getScreenshotFeedbacks() != null) {
			for (ScreenshotFeedback screenshotFeedback : feedback.getScreenshotFeedbacks()) {
				screenshotFeedback.setFeedbackId(feedbackId);
				screenshotFeedbackService.Insert(con, screenshotFeedback);
			}
		}
		return feedbackId;
	}


	private void setFeedbackRelations(Feedback feedback) throws SQLException, NotFoundException {
		feedback.setTextFeedbacks(textFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setRatings(ratingFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setScreenshots(screenshotFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
	}
}
