package ch.uzh.supersede.feedbacklibrary.feedbacks;

import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
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

    /**
     * This method returns the mechanism id.
     *
     * @return the mechanism id
     */
    public long getMechanismId() {
        return mechanismId;
    }

    /**
     * This method returns the rating.
     *
     * @return the rating
     */
    public long getRating() {
        return rating;
    }

    /**
     * This method sets the mechanism id.
     *
     * @param mechanismId the mechanism id
     */
    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    /**
     * This method sets the rating.
     *
     * @param rating the rating
     */
    public void setRating(long rating) {
        this.rating = rating;
    }
}
