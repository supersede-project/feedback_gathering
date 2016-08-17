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
	private List<RatingFeedback> ratings;
	@DbIgnore
	private List<ScreenshotFeedback> screenshots;
	@DbIgnore
	private List<FeedbackComment> feedbackComments;
	@DbIgnore
	private List<AttachmentFeedback> attachmentFeedbacks;
	@DbIgnore
	private List<AudioFeedback> audioFeedbacks;
	@DbIgnore
	private List<CategoryFeedback> categoryFeedbacks;
}
