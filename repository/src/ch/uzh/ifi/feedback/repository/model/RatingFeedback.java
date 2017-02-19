package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;

public class RatingFeedback extends ItemBase<RatingFeedback>{

	@Id
	private Integer id;
	
	private String title;
	@NotNull
	private int rating;

	@DbAttribute("feedback_id")
	private transient Integer feedbackId;

	@DbAttribute("mechanism_id")
	private Integer mechanismId;
	
	public RatingFeedback(Integer id, String title, int rating, Integer feedbackId, Integer mechanismId) {
		super();
		this.id = id;
		this.title = title;
		this.rating = rating;
		this.feedbackId = feedbackId;
		this.mechanismId = mechanismId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
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

	public Integer getMechanismId() {
		return mechanismId;
	}

	public void setMechanismId(Integer mechanismId) {
		this.mechanismId = mechanismId;
	}
}
