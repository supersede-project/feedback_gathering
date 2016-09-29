package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.RatingMechanism;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Rating feedback.
 */
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

    public long getRating() {
        return rating;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
