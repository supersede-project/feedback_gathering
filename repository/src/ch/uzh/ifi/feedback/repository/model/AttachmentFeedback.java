package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;

public class AttachmentFeedback {

	@Id
	private long id;

	@DbAttribute("feedback_id")
	private transient Integer feedbackId;

	@NotNull
	private String path;

	@NotNull
	private long size;

	@NotNull
	@DbAttribute("file_extension")
	private String fileExtension;

	private String name;

	private String part;

	@DbAttribute("mechanism_id")
	private long mechanismId;

	public AttachmentFeedback(long id, Integer feedbackId, String path, long size, String fileExtension, String name,
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public long getMechanismId() {
		return mechanismId;
	}

	public void setMechanismId(long mechanismId) {
		this.mechanismId = mechanismId;
	}
}