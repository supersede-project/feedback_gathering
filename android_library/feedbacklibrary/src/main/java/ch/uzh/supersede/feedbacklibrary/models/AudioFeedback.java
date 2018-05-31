package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

public class AudioFeedback extends AbstractMultipartFeedback {
    private String audioPath;
    private double maxTime;
    @Expose
    private int duration;

    public AudioFeedback(long mechanismId, LocalConfigurationBean configuration) {
        super(mechanismId, configuration.getAudioOrder());
        this.maxTime = configuration.getAudioMaxTime();
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    @Override
    public String getPartString() {
        return "audio";
    }

    @Override
    public String getFilePath() {
        return audioPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
        initPartFeedback(audioPath); //FIXME [jfo] not gudd
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
