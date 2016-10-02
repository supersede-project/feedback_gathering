package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.Serializable;

/**
 * Audio feedback.
 */
public class AudioFeedback extends PartFeedback implements Serializable {
    private String audioPath;
    @Expose
    private int duration;

    public AudioFeedback(AudioMechanism audioMechanism, int partId) {
        super(audioMechanism, audioMechanism.getAudioPath(), partId);
        initAudioFeedback(audioMechanism);
    }

    public String getAudioPath() {
        return audioPath;
    }

    public int getDuration() {
        return duration;
    }

    public String getFileName() {
        return new File(audioPath).getName();
    }

    @Override
    public String getPartString() {
        return "audio";
    }

    private void initAudioFeedback(AudioMechanism audioMechanism) {
        audioPath = audioMechanism.getAudioPath();
        duration = audioMechanism.getTotalDuration();
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
