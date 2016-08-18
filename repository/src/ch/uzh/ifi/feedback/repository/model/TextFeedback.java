package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;

public class TextFeedback {

	@Id
	private Integer id;

	@DbAttribute("feedback_id")
	private transient Integer feedbackId;

	private String text;

	@DbAttribute("mechanism_id")
	private long mechanismId;
	
	public TextFeedback() {}

	public TextFeedback(Integer id, Integer feedbackId, String text, long mechanismId) {
		super();
		this.id = id;
		this.feedbackId = feedbackId;
		this.text = text;
		this.mechanismId = mechanismId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getMechanismId() {
		return mechanismId;
	}

	public void setMechanismId(long mechanismId) {
		this.mechanismId = mechanismId;
	}
}
