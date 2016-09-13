package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.AudioMechanism;

import java.io.Serializable;

/**
 * Audio feedback.
 */
public class AudioFeedback extends PartFeedback implements Serializable {
    private String audioPath;

    public AudioFeedback(AudioMechanism audioMechanism, int partId) {
        super(audioMechanism, audioMechanism.getAudioPath(), partId);
        initAudioFeedback(audioMechanism);
    }

    public String getAudioPath() {
        return audioPath;
    }

    @Override
    public String getPartString() {
        return "audio";
    }

    private void initAudioFeedback(AudioMechanism audioMechanism) {
        audioPath = audioMechanism.getAudioPath();
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
