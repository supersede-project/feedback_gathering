package ch.fhnw.cere.repository.models.reporting;

import java.util.List;

public class ReportResponse {
    private List<RatingReport> ratingReports;

    public ReportResponse(List<RatingReport> ratingReports) {
        this.ratingReports = ratingReports;
    }

    @Override
    public String toString() {
        return "ReportResponse{" +
                "ratingReports=" + ratingReports +
                '}';
    }

    public List<RatingReport> getRatingReports() {
        return ratingReports;
    }

    public void setRatingReports(List<RatingReport> ratingReports) {
        this.ratingReports = ratingReports;
    }
}
