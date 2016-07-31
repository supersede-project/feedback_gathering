package com.example.matthias.feedbacklibrary.models;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;

import java.io.Serializable;
import java.util.List;

/**
 * Audio mechanism model
 */
public class AudioMechanism extends Mechanism implements Serializable {
    public AudioMechanism(MechanismConfigurationItem item) {
        super(AUDIO_TYPE, item);
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }
}
