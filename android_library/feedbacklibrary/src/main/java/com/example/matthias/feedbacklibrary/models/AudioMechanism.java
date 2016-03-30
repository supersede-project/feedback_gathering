package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public class AudioMechanism extends Mechanism implements Serializable {
    private static final String AUDIO_TYPE = "AUDIO_MECHANISM";

    public AudioMechanism(boolean canBeActivated, boolean isActive, int order) {
        super(AUDIO_TYPE, canBeActivated, isActive, order);
    }

    public void updateView() {}
    public void updateModel() {}
}
