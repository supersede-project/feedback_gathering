package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

public class ScreenshotFeedback extends AbstractMultipartFeedback {
    private boolean isEditable;
    private List<String> textAnnotations;

    public ScreenshotFeedback(LocalConfigurationBean configuration) {
        super(configuration.getScreenshotOrder());
        this.isEditable = configuration.isScreenshotEditable();
        setPart("screenshot");
        setFileExtension(".png");
    }

    public boolean isEditable() {
        return isEditable;
    }

    public List<String> getTextAnnotations() {
        return textAnnotations;
    }
}
