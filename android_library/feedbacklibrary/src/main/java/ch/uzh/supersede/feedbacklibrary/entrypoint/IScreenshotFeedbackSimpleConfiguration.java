package ch.uzh.supersede.feedbacklibrary.entrypoint;

public interface IScreenshotFeedbackSimpleConfiguration extends IFeedbackConfiguration{
    /** Setting this to -1 will disable this type of feedback.
     * @return Sorting-Order of this feedback
     */
    int getConfiguredScreenshotFeedbackOrder();
}
