package ch.uzh.supersede.feedbacklibrary.feedbacks;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;

public class RatingFeedback implements Serializable {
    @Expose
    private long mechanismId;
    @Expose
    private long rating;

    public RatingFeedback(RatingMechanism ratingMechanism) {
        setMechanismId(ratingMechanism.getId());
        setRating((long) ratingMechanism.getInputRating());
    }

    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
