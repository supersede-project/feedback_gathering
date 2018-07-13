package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackDeveloperConfiguration {

    boolean isDeveloper();

    String getConfiguredRepositoryLogin();

    String getConfiguredRepositoryPassword();
}
