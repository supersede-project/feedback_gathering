package ch.uzh.supersede.feedbacklibrary.feedback;

import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
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

    /**
     * This method returns the path to the audio file.
     *
     * @return the audio path
     */
    public String getAudioPath() {
        return audioPath;
    }

    /**
     * This method returns the duration of the audio file.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * This method returns the name of the audio file.
     *
     * @return the name
     */
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

    /**
     * This method sets the path to the audio file.
     *
     * @param audioPath the audio path
     */
    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    /**
     * This method sets the duration of the audio file.
     *
     * @param duration the duration.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
