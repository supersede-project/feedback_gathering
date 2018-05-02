package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackStyle {
    enum FEEDBACK_STYLE{
        NONE,DARK,BRIGHT,ADAPTIVE
    }

    FEEDBACK_STYLE getConfiguredFeedbackStyle();
}
