package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;

public class AttachmentFeedback extends FileFeedback {

	@DbAttribute("feedback_id")
	private transient Integer feedbackId;

	@DbAttribute("mechanism_id")
	private long mechanismId;

	public AttachmentFeedback(Integer id, Integer feedbackId, String path, int size, String fileExtension, String name,
			String part, long mechanismId) {
		super();
		this.id = id;
		this.feedbackId = feedbackId;
		this.path = path;
		this.size = size;
		this.fileExtension = fileExtension;
		this.name = name;
		this.part = part;
		this.mechanismId = mechanismId;
	}
	
	public AttachmentFeedback(){
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
}