package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

/**
 * Audio mechanism model
 */
public class AudioMechanism extends Mechanism {
    private String audioPath;
    // Default value is 10 seconds
    private float maxTime = 10.0F;
    private String title;
    private int totalDuration;

    public AudioMechanism(MechanismConfigurationItem item) {
        super(AUDIO_TYPE, item);
        initAudioMechanism(item);
    }

    /**
     * This method returns the path to the audio file.
     *
     * @return the audio path
     */
    public String getAudioPath() {
        return audioPath;
    }

    /**
     * This method returns the maximum allowed recording time.
     *
     * @return the maximum time
     */
    public float getMaxTime() {
        return maxTime;
    }

    /**
     * This method returns the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method returns the total duration of the recorded audio file.
     *
     * @return the total duration
     */
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
                setMaxTime(Float.parseFloat((String)param.get("value")));
            }
        }
        setTitle(getTitle());
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    /**
     * This method sets the path to the audio file.
     *
     * @param audioPath the audio path
     */
    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    /**
     * This method sets the maximum allowed recording time.
     *
     * @param maxTime the maximum time
     */
    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * This method sets the title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method sets the total duration of the recorded audio file.
     *
     * @param totalDuration the total duration
     */
    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
}
