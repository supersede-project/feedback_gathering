package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackStyle {
    enum FEEDBACK_STYLE{
        DARK,BRIGHT,ADAPTIVE
    }

    FEEDBACK_STYLE getConfiguredFeedbackStyle();
}
