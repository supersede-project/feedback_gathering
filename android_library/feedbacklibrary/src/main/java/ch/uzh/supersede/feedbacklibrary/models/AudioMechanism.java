package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class AudioMechanism extends AbstractPartMechanism {
    private String audioPath;
    // Default value is 10 seconds
    private float maxTime = 10;
    @Expose
    private int duration;

    public AudioMechanism(long mechanismId, int order) {
        super(mechanismId, order);
        //TODO [jfo] set max time localConfiguration
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

    public float getMaxTime() {
        return maxTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
