package ch.uzh.ifi.feedback.repository.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.Service.ItemBase;
import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;
import ch.uzh.ifi.feedback.repository.serialization.FeedbackSerializationService;

@Serialize(FeedbackSerializationService.class)
public class Feedback extends ItemBase<Feedback> {

	@NotNull
	private String title;
	@Id
	private Integer id;

	@NotNull
	@DbAttribute("application_id")
	private long applicationId;

	@DbAttribute("user_identification")
	@NotNull
	private String userIdentification;

	@DbAttribute("created_at")
	private Timestamp createdAt;

	@DbAttribute("updated_at")
	private Timestamp updatedAt;

	@DbAttribute("configuration_id")
	private long configurationId;
	
	@DbAttribute("context_informations_id")
	private transient Integer contextInformationId;
	
	private String language;

	
	@DbIgnore
	private ContextInformation contextInformation;
	@DbIgnore
	private List<TextFeedback> textFeedbacks;
	@DbIgnore
	private List<RatingFeedback> ratingFeedbacks;
	@DbIgnore
	private List<ScreenshotFeedback> screenshotFeedbacks;
	@DbIgnore
	private List<FeedbackComment> feedbackComments;
	@DbIgnore
	private List<AttachmentFeedback> attachmentFeedbacks;
	@DbIgnore
	private List<AudioFeedback> audioFeedbacks;
	@DbIgnore
	private List<CategoryFeedback> categoryFeedbacks;

	public Feedback() {}
	
	public Feedback(String title, Integer id, Integer applicationId, String userIdentification, Timestamp createdAt,
			Timestamp updatedAt, String language, ContextInformation contextInformation,
			List<TextFeedback> textFeedbacks, List<RatingFeedback> ratings, List<ScreenshotFeedback> screenshots,
			List<FeedbackComment> feedbackComments, List<AttachmentFeedback> attachmentFeedbacks,
			List<AudioFeedback> audioFeedbacks, List<CategoryFeedback> categoryFeedbacks) {
		super();
		this.title = title;
		this.id = id;
		this.applicationId = applicationId;
		this.userIdentification = userIdentification;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.language = language;
		this.contextInformation = contextInformation;
		this.textFeedbacks = textFeedbacks;
		this.ratingFeedbacks = ratings;
		this.screenshotFeedbacks = screenshots;
		this.feedbackComments = feedbackComments;
		this.attachmentFeedbacks = attachmentFeedbacks;
		this.audioFeedbacks = audioFeedbacks;
		this.categoryFeedbacks = categoryFeedbacks;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getApplicationId() {
		return applicationId;
	}
	
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
	public long getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(long configurationId) {
		this.configurationId = configurationId;
	}

	public String getUserIdentification() {
		return userIdentification;
	}

	public void setUserIdentification(String userIdentification) {
		this.userIdentification = userIdentification;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ContextInformation getContextInformation() {
		return contextInformation;
	}

	public void setContextInformation(ContextInformation contextInformation) {
		this.contextInformation = contextInformation;
	}

	public List<TextFeedback> getTextFeedbacks() {
		return textFeedbacks;
	}

	public void setTextFeedbacks(List<TextFeedback> textFeedbacks) {
		this.textFeedbacks = textFeedbacks;
	}

	public List<RatingFeedback> getRatingFeedbacks() {
		return ratingFeedbacks;
	}

	public void setRatings(List<RatingFeedback> ratingFeedbacks) {
		this.ratingFeedbacks = ratingFeedbacks;
	}

	public List<ScreenshotFeedback> getScreenshotFeedbacks() {
		return screenshotFeedbacks;
	}

	public void setScreenshots(List<ScreenshotFeedback> screenshotFeedbacks) {
		this.screenshotFeedbacks = screenshotFeedbacks;
	}

	public List<FeedbackComment> getFeedbackComments() {
		return feedbackComments;
	}

	public void setFeedbackComments(List<FeedbackComment> feedbackComments) {
		this.feedbackComments = feedbackComments;
	}

	public List<AttachmentFeedback> getAttachmentFeedbacks() {
		return attachmentFeedbacks;
	}

	public void setAttachmentFeedbacks(List<AttachmentFeedback> attachmentFeedbacks) {
		this.attachmentFeedbacks = attachmentFeedbacks;
	}

	public List<AudioFeedback> getAudioFeedbacks() {
		return audioFeedbacks;
	}

	public void setAudioFeedbacks(List<AudioFeedback> audioFeedbacks) {
		this.audioFeedbacks = audioFeedbacks;
	}

	public List<CategoryFeedback> getCategoryFeedbacks() {
		return categoryFeedbacks;
	}

	public void setCategoryFeedbacks(List<CategoryFeedback> categoryFeedbacks) {
		this.categoryFeedbacks = categoryFeedbacks;
	}

	public void addScreenshotFeedback(ScreenshotFeedback screenshotFeedback) {
		this.screenshotFeedbacks.add(screenshotFeedback);
	}

	public void addRatingFeedback(RatingFeedback ratingFeedback) {
		this.ratingFeedbacks.add(ratingFeedback);
	}

	public void addTextFeedback(TextFeedback textFeedback) {
		this.textFeedbacks.add(textFeedback);
	}

	public Integer getContextInformationId() {
		return contextInformationId;
	}

	public void setContextInformationId(Integer contextInformationId) {
		this.contextInformationId = contextInformationId;
	}
}
