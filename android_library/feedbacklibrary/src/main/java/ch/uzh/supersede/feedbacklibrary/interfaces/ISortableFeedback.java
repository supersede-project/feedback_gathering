package ch.uzh.supersede.feedbacklibrary.interfaces;

import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_SORTING;

public interface ISortableFeedback {
    void sort(FEEDBACK_SORTING sorting);
}
