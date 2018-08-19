package ch.uzh.supersede.feedbacklibrary.models;

public final class FeedbackReportRequestBody {
    private long feedbackId;
    private String reporterUsername;
    private String reason;

    public FeedbackReportRequestBody(long feedbackId, String reporterUsername, String reason) {
        this.feedbackId = feedbackId;
        this.reporterUsername = reporterUsername;
        this.reason = reason;
    }

    public long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getReporterUsername() {
        return reporterUsername;
    }

    public void setReporterUsername(String reporterUsername) {
        this.reporterUsername = reporterUsername;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}