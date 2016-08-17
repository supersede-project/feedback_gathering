package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;

public class ScreenshotFeedback {

	@Id
	private long id;
	
	@DbAttribute("feedback_id")
	private transient Integer feedbackId;
	
	@NotNull
	private String path;
	
	@NotNull
	private int size;
		
	private String name;
	
	@DbAttribute("mechanism_id")
	private long mechanismId;

	@DbIgnore
	private String part;
	
	@DbAttribute("file_extension")
	private String fileExtension;

	public ScreenshotFeedback(long id, Integer feedbackId, String path, int size, String name, long mechanismId,
			String part, String fileExtension) {
		super();
		this.id = id;
		this.feedbackId = feedbackId;
		this.path = path;
		this.size = size;
		this.name = name;
		this.mechanismId = mechanismId;
		this.part = part;
		this.fileExtension = fileExtension;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMechanismId() {
		return mechanismId;
	}

	public void setMechanismId(long mechanismId) {
		this.mechanismId = mechanismId;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
}
