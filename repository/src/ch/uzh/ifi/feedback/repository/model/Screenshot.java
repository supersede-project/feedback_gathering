package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;

public class Screenshot {
	
	@Id
	private Integer id;
	private String name;
	@NotNull
	private String path;
	@NotNull
	private int size;
	
	@DbAttribute("feedback_id")
	private transient Integer feedbackId;
	
	public Screenshot(String fileName, String path, int size)
	{
		setFileName(fileName);
		setPath(path);
		setSize(size);
	}
	
	public Screenshot(){
	}
	
	public String getFileName() {
		return name;
	}
	public void setFileName(String fileName) {
		this.name = fileName;
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
}
