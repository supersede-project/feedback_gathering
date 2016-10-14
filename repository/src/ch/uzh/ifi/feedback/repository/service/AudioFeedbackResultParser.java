package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;

@Singleton
public class AudioFeedbackResultParser extends DbResultParser<AudioFeedback>{
	
	public AudioFeedbackResultParser() {
		super(AudioFeedback.class);
	}
}
