package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;

@Singleton
public class AttachmentFeedbackService extends ServiceBase<AttachmentFeedback>{
	
	@Inject
	public AttachmentFeedbackService(AttachmentFeedbackResultParser resultParser, IDatabaseConfiguration dbConfig) {
		super(resultParser, AttachmentFeedback.class, "attachment_feedbacks", dbConfig.getDatabase());
	}
}
