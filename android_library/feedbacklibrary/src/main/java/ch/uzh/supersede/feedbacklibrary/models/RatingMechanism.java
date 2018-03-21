package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

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
            Object value = param.get("value");
            if (key == null){
                return;
            }
            // Title
            if (key.equals("title")) {
                setTitle((String) value);
            }
            // Rating icon
            if (key.equals("ratingIcon")) {
                setRatingIcon((String) value);
            }
            // Maximum rating
            if (key.equals("maxRating")) {
                Double doubleValue = Double.parseDouble((String) value);
                setDefaultRating(doubleValue.floatValue());
            }
            // Default rating
            if (key.equals("defaultRating")) {
                Double doubleValue = Double.parseDouble((String) value);
                setDefaultRating(doubleValue.floatValue());
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
