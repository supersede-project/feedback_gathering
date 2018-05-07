package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.RATING_TYPE;

public class RatingMechanism extends AbstractMechanism {
    private String ratingIcon;
    private int maxRating;
    private float defaultRating;

    @Expose
    private long rating;

    public RatingMechanism(MechanismConfigurationItem item) {
        super(RATING_TYPE, item);
    }

    @Override
    public void handleMechanismParameter(String key, Object value) {
        super.handleMechanismParameter(key, value);
        if (key.equals("ratingIcon")) {
            setRatingIcon((String) value);
        } else if (key.equals("maxRating") || key.equals("defaultRating")) {
            setDefaultRating(Float.parseFloat((String) value));
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    public String getRatingIcon() {
        return ratingIcon;
    }

    public void setRatingIcon(String ratingIcon) {
        this.ratingIcon = ratingIcon;
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

    public float getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
