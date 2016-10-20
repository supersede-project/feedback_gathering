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

    /**
     * This method returns the default rating.
     *
     * @return the default rating
     */
    public float getDefaultRating() {
        return defaultRating;
    }

    /**
     * This method returns the input rating.
     *
     * @return the input rating
     */
    public float getInputRating() {
        return inputRating;
    }

    /**
     * This method returns the maximum possible rating.
     *
     * @return the maximum rating
     */
    public int getMaxRating() {
        return maxRating;
    }

    /**
     * This method returns the rating icon.
     *
     * @return the rating icon
     */
    public String getRatingIcon() {
        return ratingIcon;
    }

    /**
     * This method returns the title
     *
     * @return the title
     */
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

    /**
     * This method sets the default rating.
     *
     * @param defaultRating the default rating
     */
    public void setDefaultRating(float defaultRating) {
        this.defaultRating = defaultRating;
    }

    /**
     * This method sets the input rating.
     *
     * @param inputRating the input rating
     */
    public void setInputRating(float inputRating) {
        this.inputRating = inputRating;
    }

    /**
     * This method sets the maximum possible rating.
     *
     * @param maxRating the maximum rating
     */
    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    /**
     * This method sets the rating icon.
     *
     * @param ratingIcon the rating icon
     */
    public void setRatingIcon(String ratingIcon) {
        this.ratingIcon = ratingIcon;
    }

    /**
     * This method sets the title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
