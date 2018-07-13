package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

public class AudioFeedback extends AbstractMultipartFeedback {
    private String audioPath;
    private double maxTime;
    @Expose
    private int duration;

    public AudioFeedback(LocalConfigurationBean configuration) {
        super(configuration.getAudioOrder());
        this.maxTime = configuration.getMaxAudioTime();
        setPart("audio");
        setFileExtension("mp3");
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
