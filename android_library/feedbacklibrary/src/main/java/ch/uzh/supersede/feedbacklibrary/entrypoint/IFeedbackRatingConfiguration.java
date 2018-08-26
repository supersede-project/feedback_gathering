package ch.uzh.supersede.feedbacklibrary.entrypoint;

import ch.uzh.supersede.feedbacklibrary.utils.ValueCheck;

@ValueCheck("(getConfiguredRatingFeedbackMaxValue() > 0)" +
        "&& (getConfiguredRatingFeedbackDefaultValue() <= getConfiguredRatingFeedbackMaxValue())")
public interface IFeedbackRatingConfiguration {
    /**
     * Setting this to -1 will disable Rating Feedback.
     *
     * @return Sorting-Order of this Feedback
     */
    int getConfiguredRatingFeedbackOrder();

    /**
     * Set maximum possible rating value.
     *
     * @return max rating value
     */
    int getConfiguredRatingFeedbackMaxValue();

    /**
     * Set a default rating value that is set when no rating is given manually.
     *
     * @return default rating value
     */
    int getConfiguredRatingFeedbackDefaultValue();
}
