package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public class ScreenshotMechanism extends Mechanism implements Serializable {
    private static final String SCREENSHOT_TYPE = "SCREENSHOT_TYPE";

    public ScreenshotMechanism(FeedbackConfigurationItem item) {
        super(SCREENSHOT_TYPE, item);
    }
}
