package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;
import java.util.List;

/**
 * Audio mechanism model
 */
public class AudioMechanism extends Mechanism implements Serializable {
    private static final String AUDIO_TYPE = "AUDIO_TYPE";

    public AudioMechanism(FeedbackConfigurationItem item) {
        super(AUDIO_TYPE, item);
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }
}
