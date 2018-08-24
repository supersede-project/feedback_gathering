package ch.uzh.supersede.feedbacklibrary.interfaces;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_SORTING;

public interface ISortableFeedback {
    void setSorting(FEEDBACK_SORTING sorting, List<Enums.FEEDBACK_STATUS> allowedStatuses);
}
