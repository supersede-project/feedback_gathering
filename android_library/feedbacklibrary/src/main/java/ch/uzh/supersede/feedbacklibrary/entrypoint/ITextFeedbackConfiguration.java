package ch.uzh.supersede.feedbacklibrary.entrypoint;

public interface ITextFeedbackConfiguration extends ITextFeedbackSimpleConfiguration {
    /**
     * A hint that is shown within the edit field.
     *
     * @return hint
     */
    String getConfiguredTextFeedbackHint();

    /**
     * Label of the edit field.
     *
     * @return label
     */
    String getConfiguredTextFeedbackLabel();

    /**
     * Set the maximum text length.
     *
     * @return maximum text length
     */
    int getConfiguredTextFeedbackMaxLength();

    /**
     * Set the minimum text length. If set to 0, providing textual feedback is optional.
     *
     * @return minimum text length
     */
    int getConfiguredTextFeedbackMinLength();

}
