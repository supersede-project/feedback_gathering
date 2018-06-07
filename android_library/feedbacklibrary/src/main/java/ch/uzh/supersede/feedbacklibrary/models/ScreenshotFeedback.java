package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

public class ScreenshotFeedback extends AbstractMultipartFeedback {
    private String imagePath;
    private boolean isEditable;

    public ScreenshotFeedback(long screenshotFeedbackId, LocalConfigurationBean configuration) {
        super(screenshotFeedbackId, configuration.getScreenshotOrder());
        this.isEditable = configuration.isScreenshotIsEditable();
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    @Override
    public String getPartString() {
        return "screenshot";
    }

    @Override
    public String getFilePath() {
        return imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isEditable() {
        return isEditable;
    }
}
