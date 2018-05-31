package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackSettings {

    int getConfiguredMinUserNameLength();

    int getConfiguredMaxUserNameLength();

    int getConfiguredMinResponseLength();

    int getConfiguredMaxResponseLength();

}
