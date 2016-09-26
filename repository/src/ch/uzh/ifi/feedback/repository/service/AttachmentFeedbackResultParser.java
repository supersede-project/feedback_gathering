package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;

public class AttachmentFeedbackResultParser extends DbResultParser<AttachmentFeedback>{
	
	public AttachmentFeedbackResultParser() {
		super(AttachmentFeedback.class);
	}
}
