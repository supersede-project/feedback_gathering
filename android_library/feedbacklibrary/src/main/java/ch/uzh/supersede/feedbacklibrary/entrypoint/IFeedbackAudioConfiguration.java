package ch.uzh.supersede.feedbacklibrary.entrypoint;

public interface IFeedbackAudioConfiguration {

    /**
     * Setting this to -1 will disable Audio Feedback.
     *
     * @return Sorting-Order of this Feedback
     */
    int getConfiguredAudioFeedbackOrder();

    /**
     * Defines how long audio captures may be at most.
     *
     * @return seconds of maximum audio recording
     */
    double getConfiguredAudioFeedbackMaxTime();
}
