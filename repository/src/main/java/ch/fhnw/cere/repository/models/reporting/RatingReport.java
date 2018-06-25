package ch.fhnw.cere.repository.models.reporting;

public class RatingReport {
    private String ratingTitle;
    private long ratingMechanismId;
    private Float averageRating;
    private int numberOfRatings;
    private Float minRating;
    private Float maxRating;

    public RatingReport() {
    }

    public RatingReport(String ratingTitle, long ratingMechanismId, Float averageRating, int numberOfRatings,
                        Float minRating, Float maxRating) {
        this.ratingTitle = ratingTitle;
        this.ratingMechanismId = ratingMechanismId;
        this.averageRating = averageRating;
        this.numberOfRatings = numberOfRatings;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    @Override
    public String toString() {
        return "RatingReport{" +
                "ratingTitle='" + ratingTitle + '\'' +
                ", ratingMechanismId=" + ratingMechanismId +
                ", averageRating=" + averageRating +
                ", numberOfRatings=" + numberOfRatings +
                ", minRating=" + minRating +
                ", maxRating=" + maxRating +
                '}';
    }

    public String getRatingTitle() {
        return ratingTitle;
    }

    public void setRatingTitle(String ratingTitle) {
        this.ratingTitle = ratingTitle;
    }

    public long getRatingMechanismId() {
        return ratingMechanismId;
    }

    public void setRatingMechanismId(long ratingMechanismId) {
        this.ratingMechanismId = ratingMechanismId;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public Float getMinRating() {
        return minRating;
    }

    public void setMinRating(Float minRating) {
        this.minRating = minRating;
    }

    public Float getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Float maxRating) {
        this.maxRating = maxRating;
    }
}
