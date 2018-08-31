package ch.uzh.supersede.feedbacklibrary.entrypoint;

public interface IFeedbackScreenshotConfiguration {
    /**
     * Setting this to -1 will disable Screenshot Feedback.
     *
     * @return Sorting-Order of this Feedback
     */
    int getConfiguredScreenshotFeedbackOrder();

    /**
     * Whether the screenshot shall be editable.
     *
     * @return true if editable
     */
    boolean getConfiguredScreenshotFeedbackIsEditable();
}
