package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;


public class CategoryFeedback extends ItemBase<CategoryFeedback> {

	@Id
	private Integer id;

	@DbAttribute("feedback_id")
	private transient Integer feedbackId;
	
	@DbAttribute("parameter_id")
	private Integer parameterId;
	
	@DbAttribute("text")
	private String text;
	
	public CategoryFeedback(Integer id, Integer feedbackId, Integer parameterId) {
		super();
		this.id = id;
		this.feedbackId = feedbackId;
		this.parameterId = parameterId;
	}
	
	public CategoryFeedback(Integer id, Integer feedbackId, Integer parameterId, String text) {
		super();
		this.id = id;
		this.feedbackId = feedbackId;
		this.parameterId = parameterId;
		this.text = text;
	}

	public CategoryFeedback(){
	}

	public Integer getId() {
		return id;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
}
