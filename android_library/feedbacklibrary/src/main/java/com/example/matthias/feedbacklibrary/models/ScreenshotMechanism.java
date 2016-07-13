package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Screenshot mechanism model
 */
public class ScreenshotMechanism extends Mechanism implements Serializable {
    private static final String SCREENSHOT_TYPE = "SCREENSHOT_TYPE";

    private String title;
    private String defaultPicture;

    private String imagePath;

    public ScreenshotMechanism(FeedbackConfigurationItem item) {
        super(SCREENSHOT_TYPE, item);
    }

    public String getDefaultPicture() {
        return defaultPicture;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setDefaultPicture(String defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
