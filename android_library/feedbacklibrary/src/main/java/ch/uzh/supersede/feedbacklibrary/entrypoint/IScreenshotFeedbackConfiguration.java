package ch.uzh.supersede.feedbacklibrary.entrypoint;

public interface IScreenshotFeedbackConfiguration extends IScreenshotFeedbackSimpleConfiguration {
    /**
     * Whether the screenshot shall be editable.
     *
     * @return true if editable
     */
    boolean getConfiguredScreenshotFeedbackIsEditable();
}
