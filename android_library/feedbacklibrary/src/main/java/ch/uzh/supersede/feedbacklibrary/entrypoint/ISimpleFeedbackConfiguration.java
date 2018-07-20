package ch.uzh.supersede.feedbacklibrary.entrypoint;

/**
 * Simple interface to activate/deactivate the specific feedbackPart<br>
 * A specific interface will override the corresponding part in this interface
 */
public interface ISimpleFeedbackConfiguration {
        /** Setting this to -1 will disable Audio Feedback.
         * @return Sorting-Order of this Feedback
         */
        int getConfiguredAudioFeedbackOrder();

        /** Setting this to -1 will disable Screenshot Feedback.
         * @return Sorting-Order of this Feedback
         */
        int getConfiguredScreenshotFeedbackOrder();

        /** Setting this to -1 will disable Label Feedback.
         * @return Sorting-Order of this Feedback
         */
        int getConfiguredLabelFeedbackOrder();

        /** Setting this to -1 will disable Text Feedback.
         * @return Sorting-Order of this Feedback
         */
        int getConfiguredTextFeedbackOrder();

        /** Setting this to -1 will disable Rating Feedback.
         * @return Sorting-Order of this Feedback
         */
        int getConfiguredRatingFeedbackOrder();
}
