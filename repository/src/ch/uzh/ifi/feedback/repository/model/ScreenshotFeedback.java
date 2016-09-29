package ch.uzh.ifi.feedback.repository.model;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;

public class ScreenshotFeedback extends FileFeedback {

	@DbAttribute("feedback_id")
	private transient Integer feedbackId;

	@DbAttribute("mechanism_id")
	private Integer mechanismId;
	
	@DbIgnore
	private List<TextAnnotation> textAnnotations;

	public ScreenshotFeedback() {
		setTextAnnotations(new ArrayList<>());
	}

	public ScreenshotFeedback(Integer id, Integer feedbackId, String path, int size, String name, Integer mechanismId,
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
		setTextAnnotations(new ArrayList<>());
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

	public List<TextAnnotation> getTextAnnotations() {
		if(textAnnotations == null)
			textAnnotations = new ArrayList<>();
		
		return textAnnotations;
	}

	public void setTextAnnotations(List<TextAnnotation> textAnnotations) {
		this.textAnnotations = textAnnotations;
	}
}
