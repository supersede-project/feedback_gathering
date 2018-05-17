package ch.uzh.supersede.feedbacklibrary.entrypoint;

public interface IAudioFeedbackConfiguration extends IAudioFeedbackSimpleConfiguration {
    /**
     * Defines how long audio captures may be at most.
     *
     * @return seconds of maximum audio recording
     */
    double getConfiguredAudioFeedbackMaxTime();
}
