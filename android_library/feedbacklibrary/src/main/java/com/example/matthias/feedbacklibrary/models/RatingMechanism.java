package com.example.matthias.feedbacklibrary.models;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Rating mechanism model
 */
public class RatingMechanism extends Mechanism {
    private String title;
    private String ratingIcon;
    private int maxRating;
    private float defaultRating;

    private float inputRating;

    public RatingMechanism(MechanismConfigurationItem item) {
        super(RATING_TYPE, item);
        initRatingMechanism(item);
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

    private void initRatingMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if (key.equals("title")) {
                setTitle((String) param.get("value"));
            }
            // Rating icon
            if (key.equals("ratingIcon")) {
                setRatingIcon((String) param.get("value"));
            }
            // Maximum rating
            if (key.equals("maxRating")) {
                setMaxRating(((Double) param.get("value")).intValue());
            }
            // Default rating
            if (key.equals("defaultRating")) {
                setDefaultRating(((Double) param.get("value")).floatValue());
            }
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
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
