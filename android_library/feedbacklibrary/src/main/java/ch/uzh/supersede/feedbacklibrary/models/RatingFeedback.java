package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

public class RatingFeedback extends AbstractFeedbackPart {
    private String ratingIcon;
    private int maxRating;
    private int defaultRating;

    @Expose
    private long rating;

    public RatingFeedback(long ratingFeedbackId, LocalConfigurationBean configuration) {
        super(ratingFeedbackId, configuration.getRatingOrder());
        this.ratingIcon = configuration.getRatingIcon();
        this.maxRating = configuration.getRatingMaxValue();
        this.defaultRating = configuration.getRatingDefaultValue();
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    public String getRatingIcon() {
        return ratingIcon;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public float getDefaultRating() {
        return defaultRating;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
