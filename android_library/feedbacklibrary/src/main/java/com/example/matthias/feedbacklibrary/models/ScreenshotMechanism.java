package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public class ScreenshotMechanism extends Mechanism implements Serializable {
    private static final String SCREENSHOT_TYPE = "SCREENSHOT_MECHANISM";

    public ScreenshotMechanism(boolean canBeActivated, boolean isActive, int order) {
        super(SCREENSHOT_TYPE, canBeActivated, isActive, order);
    }
}
