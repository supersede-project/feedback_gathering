package com.example.matthias.feedbacklibrary.models;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;

import java.util.List;
import java.util.Map;

/**
 * Audio mechanism model
 */
public class AudioMechanism extends Mechanism {
    private String audioPath;
    private float maxTimeDefault;
    private float maxTime;
    private String title;
    private int totalDuration;

    public AudioMechanism(MechanismConfigurationItem item) {
        super(AUDIO_TYPE, item);
        initAudioMechanism(item);
    }

    public String getAudioPath() {
        return audioPath;
    }

    public float getMaxTime() {
        return maxTime;
    }

    public float getMaxTimeDefault() {
        return maxTimeDefault;
    }

    public String getTitle() {
        return title;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    private void initAudioMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if (key.equals("title")) {
                setTitle((String) param.get("value"));
            }
            // Maximum time
            if (key.equals("maxTime")) {
                setMaxTime(((Double) param.get("value")).floatValue());
                setMaxTimeDefault(Float.valueOf((String) param.get("defaultValue")));
            }
        }
        setTitle(getTitle());
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

    public void setMaxTimeDefault(float maxTimeDefault) {
        this.maxTimeDefault = maxTimeDefault;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
}
