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

    public float getInputRating() {
        return inputRating;
    }
    public void setInputRating(float inputRating) {
        this.inputRating = inputRating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public float getDefaultRating() {
        return defaultRating;
    }

    public void setDefaultRating(float defaultRating) {
        this.defaultRating = defaultRating;
    }

    public String getRatingIcon() {
        return ratingIcon;
    }

    public void setRatingIcon(String ratingIcon) {
        this.ratingIcon = ratingIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
