package ch.uzh.ifi.feedback.repository.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;
import javassist.NotFoundException;

public class FeedbackService extends ServiceBase<Feedback> {

	private TextFeedbackService textFeedbackService;
	private RatingFeedbackService ratingFeedbackService;
	private ScreenshotFeedbackService screenshotFeedbackService;
	private AudioFeedbackService audioFeedbackService;
	private AttachmentFeedbackService attachmentFeedbackService;
	private CategoryFeedbackService categoryFeedbackService;
	
	@Inject
	public FeedbackService(
			FeedbackResultParser resultParser, 
			RatingFeedbackService ratingFeedbackService,
			ScreenshotFeedbackService screenshotFeedbackService, 
			TextFeedbackService textFeedbackService,
			AudioFeedbackService audioFeedbackService,
			AttachmentFeedbackService attachmentFeedbackService,
			CategoryFeedbackService categoryFeedbackService,
			DatabaseConfiguration dbConfig) 
	{
		super(resultParser, Feedback.class, "feedbacks", dbConfig.getRepositoryDb(), ratingFeedbackService, screenshotFeedbackService);
		
		this.screenshotFeedbackService = screenshotFeedbackService;
		this.ratingFeedbackService = ratingFeedbackService;
		this.textFeedbackService = textFeedbackService;
		this.audioFeedbackService = audioFeedbackService;
		this.attachmentFeedbackService = attachmentFeedbackService;
		this.categoryFeedbackService = categoryFeedbackService;
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
		
		if(feedback.getAudioFeedbacks() != null) {
			for (AudioFeedback audioFeedback : feedback.getAudioFeedbacks()) {
				audioFeedback.setFeedbackId(feedbackId);
				audioFeedbackService.Insert(con, audioFeedback);
			}
		}
		
		if(feedback.getAttachmentFeedbacks() != null) {
			for (AttachmentFeedback attachementFeedback : feedback.getAttachmentFeedbacks()) {
				attachementFeedback.setFeedbackId(feedbackId);
				attachmentFeedbackService.Insert(con, attachementFeedback);
			}
		}
		
		if(feedback.getCategoryFeedbacks() != null)
		{
			for(CategoryFeedback categoryFeedback : feedback.getCategoryFeedbacks())
			{
				categoryFeedback.setFeedbackId(feedbackId);
				categoryFeedbackService.Insert(con, categoryFeedback);
			}
		}
		
		return feedbackId;
	}


	private void setFeedbackRelations(Feedback feedback) throws SQLException, NotFoundException {
		feedback.setTextFeedbacks(textFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setRatings(ratingFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setScreenshots(screenshotFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setAudioFeedbacks(audioFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setAttachmentFeedbacks(attachmentFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setCategoryFeedbacks(categoryFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
	}
}
