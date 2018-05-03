package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackLayoutConfiguration {
    /** Setting this to -1 will disable Audio Feedback.
     * @return Sorting-Order of this Feedback
     */
    int getConfiguredAudioFeedbackOrder();

    /** Setting this to -1 will disable Screenshot Feedback.
     * @return Sorting-Order of this Feedback
     */
    int getConfiguredScreenshotFeedbackOrder();

    /** Setting this to -1 will disable Category Feedback.
     * @return Sorting-Order of this Feedback
     */
    int getConfiguredCategoryFeedbackOrder();

    /** Setting this to -1 will disable Text Feedback.
     * @return Sorting-Order of this Feedback
     */
    int getConfiguredTextFeedbackOrder();

    /** Setting this to -1 will disable Rating Feedback.
     * @return Sorting-Order of this Feedback
     */
    int getConfiguredRatingFeedbackOrder();
}
