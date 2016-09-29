package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;

public class AudioFeedbackService extends ServiceBase<AudioFeedback>{
	@Inject
	public AudioFeedbackService(AudioFeedbackResultParser resultParser, DatabaseConfiguration dbConfig) {
		super(resultParser, AudioFeedback.class, "audio_feedbacks", dbConfig.getRepositoryDb());
	}
}
