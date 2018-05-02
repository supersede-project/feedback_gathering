package ch.uzh.supersede.feedbacklibrary.entrypoint;


public interface IFeedbackLayoutConfiguration {

    boolean isAudioFeedbackEnabled();

    boolean isScreenshotFeedbackEnabled();

    boolean isAttachmentFeedbackEnabled();

    boolean isLabelFeedbackEnabled();

    boolean isTextFeedbackEnabled();

    int getConfiguredAudioFeedbackOrder();

    int getConfiguredScreenshotFeedbackOrder();

    int getConfiguredLabelFeedbackOrder();

    int getConfiguredTextFeedbackOrder();
}
