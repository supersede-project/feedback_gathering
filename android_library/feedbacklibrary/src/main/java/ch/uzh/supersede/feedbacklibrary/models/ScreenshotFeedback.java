package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

public class ScreenshotFeedback extends AbstractMultipartFeedback {
    private boolean isEditable;
    private List<String> textAnnotations;

    public ScreenshotFeedback(LocalConfigurationBean configuration) {
        super(configuration.getScreenshotOrder());
        this.isEditable = configuration.isScreenshotIsEditable();
    }

    public boolean isEditable() {
        return isEditable;
    }

    public List<String> getTextAnnotations() {
        return textAnnotations;
    }
}
