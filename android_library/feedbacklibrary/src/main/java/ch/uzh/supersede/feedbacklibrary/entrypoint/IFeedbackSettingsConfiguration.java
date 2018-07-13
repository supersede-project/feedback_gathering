package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackSettingsConfiguration {

    int getConfiguredMinUserNameLength();

    int getConfiguredMaxUserNameLength();

    int getConfiguredMinResponseLength();

    int getConfiguredMaxResponseLength();

    int getConfiguredMinReportLength();

    int getConfiguredMaxReportLength();

    boolean getConfiguredReportEnabled();
}
