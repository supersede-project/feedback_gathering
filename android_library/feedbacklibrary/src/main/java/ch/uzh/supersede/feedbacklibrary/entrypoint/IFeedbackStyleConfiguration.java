package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackStyleConfiguration {
    enum FEEDBACK_STYLE{
        DARK, LIGHT, ADAPTIVE, SWITZERLAND, YUGOSLAVIA, ITALY, FRANCE, GERMANY, AUSTRIA, WINDOWS95, CUSTOM
    }

    FEEDBACK_STYLE getConfiguredFeedbackStyle();

    /**
     * Only configure this when using FEEDBACK_STYLE.CUSTOM<br>
     * @return exactly 2 or 3 values for colors
     */
    int[] getConfiguredCustomStyle();
}
