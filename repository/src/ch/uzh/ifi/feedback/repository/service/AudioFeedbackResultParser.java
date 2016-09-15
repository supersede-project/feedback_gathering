package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;

public class AudioFeedbackResultParser extends DbResultParser<AudioFeedback>{
	
	public AudioFeedbackResultParser() {
		super(AudioFeedback.class);
	}
}
