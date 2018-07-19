package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackStyleConfiguration {
    enum FEEDBACK_STYLE{
        DARK, LIGHT, ADAPTIVE, SWITZERLAND, YUGOSLAVIA, ITALY, FRANCE, GERMANY, AUSTRIA, WINDOWS95
    }

    FEEDBACK_STYLE getConfiguredFeedbackStyle();
}
