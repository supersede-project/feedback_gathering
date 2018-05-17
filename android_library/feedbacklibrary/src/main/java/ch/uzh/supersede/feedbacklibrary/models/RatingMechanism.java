package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class RatingMechanism extends AbstractMechanism {
    private String ratingIcon;
    private int maxRating;
    private float defaultRating;

    @Expose
    private long rating;

    public RatingMechanism(long mechanismId, int order) {
        super(mechanismId, order);
        //TODO [jfo] ratingIcon, maxRating, defaultRating
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
