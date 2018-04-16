package ch.uzh.supersede.feedbacklibrary.utils;

import android.graphics.Color;

public class ColorUtility {
    public static int percentToColor(float percent) {
        return Color.rgb(255 - NumberUtility.multiply(255, percent), NumberUtility.multiply(255, percent), 0);
    }
}
