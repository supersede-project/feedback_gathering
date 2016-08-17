package ch.uzh.ifi.feedback.repository.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;
import ch.uzh.ifi.feedback.repository.serialization.FeedbackSerializationService;

@Serialize(FeedbackSerializationService.class)
public class Feedback {

	@NotNull
	private String title;
	@Id
	private Integer id;

	@NotNull
	@DbAttribute("application_id")
	private Integer applicationId;

	@DbAttribute("user_identification")
	@NotNull
	private String user;

	@DbAttribute("created_at")
	private Timestamp createdAt;

	@DbAttribute("updated_at")
	private Timestamp updatedAt;

	@DbAttribute("config_version")
	@NotNull
	private Double configVersion;
	private String language;

	@DbAttribute("context_information_id")
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

	public Feedback(String title, Integer id, Integer applicationId, String user, Timestamp createdAt,
			Timestamp updatedAt, Double configVersion, String language, ContextInformation contextInformation,
			List<TextFeedback> textFeedbacks, List<RatingFeedback> ratings, List<ScreenshotFeedback> screenshots,
			List<FeedbackComment> feedbackComments, List<AttachmentFeedback> attachmentFeedbacks,
			List<AudioFeedback> audioFeedbacks, List<CategoryFeedback> categoryFeedbacks) {
		super();
		this.title = title;
		this.id = id;
		this.applicationId = applicationId;
		this.user = user;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.configVersion = configVersion;
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

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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

	public Double getConfigVersion() {
		return configVersion;
	}

	public void setConfigVersion(Double configVersion) {
		this.configVersion = configVersion;
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
}
