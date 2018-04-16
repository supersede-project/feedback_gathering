package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.MechanismConstants.AUDIO_TYPE;

public class AudioMechanism extends AbstractMechanism {
    private String audioPath;
    // Default value is 10 seconds
    private float maxTime = 10;
    private int totalDuration;

    public AudioMechanism(MechanismConfigurationItem item) {
        super(AUDIO_TYPE, item);
    }

    @Override
    public void handleMechanismParameter(String key, String value) {
        super.handleMechanismParameter(key, value);
        if (key.equals("maxTime")) {
            setMaxTime(Float.parseFloat(value));
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
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

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
}
