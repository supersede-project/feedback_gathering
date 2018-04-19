package ch.uzh.supersede.feedbacklibrary.interfaces;

public interface ISortableFeedback {
    enum FEEDBACK_SORTING {
        NONE, MINE, HOT, TOP, NEW
    }

    void sort(FEEDBACK_SORTING sorting);
}
