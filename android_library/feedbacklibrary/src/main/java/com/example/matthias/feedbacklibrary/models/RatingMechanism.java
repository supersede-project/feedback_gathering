package com.example.matthias.feedbacklibrary.models;

import android.widget.RatingBar;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.R;

import java.io.Serializable;

/**
 * Rating mechanism model
 */
public class RatingMechanism extends Mechanism implements Serializable {
    private static final String RATING_TYPE = "RATING_TYPE";

    private String title;
    private String ratingIcon;
    private int maxRating;
    private float defaultRating;

    private float inputRating;

    public RatingMechanism(FeedbackConfigurationItem item) {
        super(RATING_TYPE, item);
    }

    public float getDefaultRating() {
        return defaultRating;
    }

    public float getInputRating() {
        return inputRating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public String getRatingIcon() {
        return ratingIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setDefaultRating(float defaultRating) {
        this.defaultRating = defaultRating;
    }

    public void setInputRating(float inputRating) {
        this.inputRating = inputRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public void setRatingIcon(String ratingIcon) {
        this.ratingIcon = ratingIcon;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
