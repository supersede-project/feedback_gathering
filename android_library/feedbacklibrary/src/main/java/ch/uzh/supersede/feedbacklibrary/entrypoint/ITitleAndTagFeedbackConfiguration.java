package ch.uzh.supersede.feedbacklibrary.entrypoint;

public interface ITitleAndTagFeedbackConfiguration {

    int getConfiguredMinTitleLength();

    int getConfiguredMaxTitleLength();

    int getConfiguredMinTagLength();

    int getConfiguredMaxTagLength();

    int getConfiguredMinTagNumber();

    int getConfiguredMaxTagNumber();

    int getConfiguredMaxTagRecommendationNumber();


}
