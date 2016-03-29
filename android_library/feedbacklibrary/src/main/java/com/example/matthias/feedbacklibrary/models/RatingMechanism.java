package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public class RatingMechanism extends Mechanism implements Serializable {
    private static final String RATING_TYPE = "RATING_MECHANISM";

    private String title;
    private String ratingIcon;
    private int maxRating;
    private float defaultRating;

    public RatingMechanism(boolean canBeActivated, boolean isActive, int order) {
        super(RATING_TYPE, canBeActivated, isActive, order);
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
