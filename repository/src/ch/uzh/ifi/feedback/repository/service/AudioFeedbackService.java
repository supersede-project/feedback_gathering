package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;

@Singleton
public class AudioFeedbackService extends ServiceBase<AudioFeedback>{
	@Inject
	public AudioFeedbackService(AudioFeedbackResultParser resultParser, DatabaseConfiguration dbConfig) {
		super(resultParser, AudioFeedback.class, "audio_feedbacks", dbConfig.getRepositoryDb());
	}
}
