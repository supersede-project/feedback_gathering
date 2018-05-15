package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;

public class ScreenshotMechanism extends AbstractPartMechanism {
    private String defaultPicture;
    private String imagePath;

    public ScreenshotMechanism(long mechanismId, int order) {
        super(mechanismId, order);
        //TODO [jfo], defaultPicture
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

    public String getDefaultPicture() {
        return defaultPicture;
    }

    public String getImagePath() {
        return imagePath;
    }
}
