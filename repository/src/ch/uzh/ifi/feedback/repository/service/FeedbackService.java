package ch.uzh.ifi.feedback.repository.service;

import static java.util.Arrays.asList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.Status;
import ch.uzh.ifi.feedback.repository.model.StatusOption;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;
import javassist.NotFoundException;

public class FeedbackService extends ServiceBase<Feedback> {

	private TextFeedbackService textFeedbackService;
	private RatingFeedbackService ratingFeedbackService;
	private ScreenshotFeedbackService screenshotFeedbackService;
	private AudioFeedbackService audioFeedbackService;
	private AttachmentFeedbackService attachmentFeedbackService;
	private CategoryFeedbackService categoryFeedbackService;
	private ContextInformationService contextInformationService;
	private StatusOptionService optionService;
	private ApiUserService userService;
	private StatusService statusService;
	
	@Inject
	public FeedbackService(
			FeedbackResultParser resultParser, 
			RatingFeedbackService ratingFeedbackService,
			ScreenshotFeedbackService screenshotFeedbackService, 
			TextFeedbackService textFeedbackService,
			AudioFeedbackService audioFeedbackService,
			AttachmentFeedbackService attachmentFeedbackService,
			CategoryFeedbackService categoryFeedbackService,
			ContextInformationService contextInformationService,
			StatusOptionService optionService,
			ApiUserService userService,
			StatusService statusService,
			IDatabaseConfiguration dbConfig) 
	{
		super(  resultParser, 
				Feedback.class, 
				"feedbacks", 
				dbConfig.getDatabase());
		
		this.screenshotFeedbackService = screenshotFeedbackService;
		this.ratingFeedbackService = ratingFeedbackService;
		this.textFeedbackService = textFeedbackService;
		this.audioFeedbackService = audioFeedbackService;
		this.attachmentFeedbackService = attachmentFeedbackService;
		this.categoryFeedbackService = categoryFeedbackService;
		this.contextInformationService = contextInformationService;
		this.optionService = optionService;
		this.userService = userService;
		this.statusService = statusService;
	}
	
	@Override
	public Feedback GetById(int id) throws SQLException, NotFoundException {
		Feedback feedback = super.GetById(id);
		setFeedbackRelations(feedback);

		return feedback;
	}

	@Override
	public List<Feedback> GetAll() throws SQLException {
		List<Feedback> feedbacks = super.GetAll();

		for (Feedback feedback : feedbacks) {
			setFeedbackRelations(feedback);
		}

		return feedbacks;
	}

	@Override
	public int Insert(Connection con, Feedback feedback) throws SQLException, NotFoundException, UnsupportedOperationException {
		
		if(feedback.getContextInformation() != null)
		{
			int contextId = contextInformationService.Insert(con, feedback.getContextInformation());
			feedback.setContextInformationId(contextId);
		}
		
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
		
		//get initial non user specific status
		StatusOption initialStatus = optionService.GetWhere(asList(1, false), "`order` = ?", "user_specific = ?").get(0);		
		//get initial user specific status
		StatusOption initialUserStatus = optionService.GetWhere(asList(1, true), "`order` = ?", "user_specific = ?").get(0);
	
		//Insert initial status for users and initial general status
		Status status = new Status(null, feedbackId, initialStatus.getName());
		statusService.Insert(con, status);
		
		status.setStatus(initialUserStatus.getName());
		List<ApiUser> users = userService.GetAll();
		for(ApiUser user : users)
		{
			status.setApiUserId(user.getId());
			statusService.Insert(con, status);
		}
		
		return feedbackId;
	}


	private void setFeedbackRelations(Feedback feedback) throws SQLException {
		feedback.setTextFeedbacks(textFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setRatings(ratingFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setScreenshots(screenshotFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setAudioFeedbacks(audioFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setAttachmentFeedbacks(attachmentFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		feedback.setCategoryFeedbacks(categoryFeedbackService.GetWhere(Arrays.asList(feedback.getId()), "feedback_id = ?"));
		try {
			if(feedback.getContextInformationId() != null)
				feedback.setContextInformation(contextInformationService.GetById(feedback.getContextInformationId()));
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
}
