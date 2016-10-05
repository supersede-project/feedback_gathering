package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;
import ch.uzh.ifi.feedback.repository.serialization.StatusSerializationService;


@Serialize(StatusSerializationService.class)
public class Status extends ItemBase<Status> {

	@Id
	private Integer id;

	@DbAttribute("api_user_id")
	private transient Integer apiUserId;
	
	@DbAttribute("feedback_id")
	private transient Integer feedbackId;
	
	@NotNull
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApiUserId() {
		return apiUserId;
	}

	public void setApiUserId(Integer apiUserId) {
		this.apiUserId = apiUserId;
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
