package ch.uzh.ifi.feedback.repository.model;

import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.validation.Id;


public class CategoryFeedback {

	@Id
	private Integer id;

	@DbAttribute("feedback_id")
	private transient Integer feedbackId;
	
	@DbAttribute("mechanism_id")
	private long mechanismId;
	
	@DbIgnore
	private List<Category> categories;
	
	public CategoryFeedback(Integer id, Integer feedbackId, long mechanismId, List<Category> categories) {
		super();
		this.id = id;
		this.feedbackId = feedbackId;
		this.mechanismId = mechanismId;
		this.categories = categories;
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

	public long getMechanismId() {
		return mechanismId;
	}

	public void setMechanismId(long mechanismId) {
		this.mechanismId = mechanismId;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
