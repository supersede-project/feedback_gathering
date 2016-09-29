package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;

@Singleton
public class AttachmentFeedbackResultParser extends DbResultParser<AttachmentFeedback>{
	
	public AttachmentFeedbackResultParser() {
		super(AttachmentFeedback.class);
	}
}
