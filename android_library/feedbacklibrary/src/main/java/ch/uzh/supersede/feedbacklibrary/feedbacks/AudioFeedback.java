package ch.uzh.supersede.feedbacklibrary.feedbacks;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;

public class AudioFeedback extends AbstractPartFeedback implements Serializable {
    private String audioPath;
    @Expose
    private int duration;

    public AudioFeedback(AudioMechanism audioMechanism, int partId) {
        super(audioMechanism, audioMechanism.getAudioPath(), partId);
        initAudioFeedback(audioMechanism);
    }

    private void initAudioFeedback(AudioMechanism audioMechanism) {
        audioPath = audioMechanism.getAudioPath();
        duration = audioMechanism.getTotalDuration();
    }

    @Override
    public String getPartString() {
        return "audio";
    }

    public String getFileName() {
        return new File(audioPath).getName();
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
