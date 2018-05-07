package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.AUDIO_TYPE;

public class AudioMechanism extends AbstractPartMechanism {
    private String audioPath;
    // Default value is 10 seconds
    private float maxTime = 10;
    @Expose
    private int duration;

    public AudioMechanism(MechanismConfigurationItem item) {
        super(AUDIO_TYPE, item, 0); //TODO [jfo] set part id
    }

    @Override
    public void handleMechanismParameter(String key, Object value) {
        super.handleMechanismParameter(key, value);
        if (key.equals("maxTime")) {
            setMaxTime(Float.parseFloat((String) value));
        }
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
    }

    public float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
