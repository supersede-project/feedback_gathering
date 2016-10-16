package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;

public class AudioFeedback extends FileFeedback
{
	@DbAttribute("feedback_id")
	private transient Integer feedbackId;
	
	private int duration;
	
	@DbAttribute("mechanism_id")
	private long mechanismId;

	public AudioFeedback(Integer id, Integer feedbackId, String path, int size, String name, int duration,
			long mechanismId, String part, String fileExtension) {
		super();
		this.id = id;
		this.feedbackId = feedbackId;
		this.path = path;
		this.size = size;
		this.name = name;
		this.duration = duration;
		this.mechanismId = mechanismId;
		this.part = part;
		this.fileExtension = fileExtension;
	}
	
	public AudioFeedback(){
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getMechanismId() {
		return mechanismId;
	}

	public void setMechanismId(long mechanismId) {
		this.mechanismId = mechanismId;
	}	
}
