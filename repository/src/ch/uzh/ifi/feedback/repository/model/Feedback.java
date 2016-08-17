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
	
	private String text;
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
	private List<Rating> ratings;
	@DbIgnore
	private List<Screenshot> screenshots;
	@DbIgnore
	private List<FeedbackComment> feedbackComments;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Timestamp getCreated() {
		return createdAt;
	}

	public void setCreated(Timestamp created) {
		this.createdAt = created;
	}

	public Timestamp getLastUpdated() {
		return updatedAt;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.updatedAt = lastUpdated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getConfigVersion() {
		return configVersion;
	}

	public void setConfigVersion(Double configVersion) {
		this.configVersion = configVersion;
	}

	public List<Rating> getRatings() {
		if (ratings == null)
			ratings = new ArrayList<>();

		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public List<Screenshot> getScreenshots() {
		if (screenshots == null)
			screenshots = new ArrayList<>();

		return screenshots;
	}

	public void setScreenshots(List<Screenshot> screenshots) {
		this.screenshots = screenshots;
	}

	public List<FeedbackComment> getFeedbackComments() {
		return feedbackComments;
	}

	public void setFeedbackComments(List<FeedbackComment> feedbackComments) {
		this.feedbackComments = feedbackComments;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
