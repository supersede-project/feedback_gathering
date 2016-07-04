package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Audio mechanism model
 */
public class AudioMechanism extends Mechanism implements Serializable {
    private static final String AUDIO_TYPE = "AUDIO_TYPE";

    public AudioMechanism(FeedbackConfigurationItem item) {
        super(AUDIO_TYPE, item);
    }
}
