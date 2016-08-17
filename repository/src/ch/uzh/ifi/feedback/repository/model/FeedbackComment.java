package ch.uzh.ifi.feedback.repository.model;

import java.sql.Timestamp;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;

public class FeedbackComment {

	@Id
	private long id;

	@DbAttribute("feedback_id")
	private transient long feedbackId;

	@NotNull
	private String comment;

	@DbAttribute("user_identification")
	private String userIdentification;

	@DbAttribute("created_at")
	private Timestamp createdAt;
}