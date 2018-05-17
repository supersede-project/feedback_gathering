package ch.uzh.supersede.feedbacklibrary.entrypoint;

public interface IAudioFeedbackSimpleConfiguration extends IFeedbackConfiguration {
    /**
     * Setting this to -1 will disable this type of feedback.
     *
     * @return sorting-order of this feedback
     */
    int getConfiguredAudioFeedbackOrder();
}
