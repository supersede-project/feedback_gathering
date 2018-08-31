package ch.uzh.supersede.feedbacklibrary.models;

public final class FeedbackReport {
    private long id;
    private Feedback feedback;
    private AndroidUser reporter;
    private String reason;

    public FeedbackReport(Feedback feedback, AndroidUser reporter, String reason) {
        this.feedback = feedback;
        this.reporter = reporter;
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public AndroidUser getReporter() {
        return reporter;
    }

    public String getReason() {
        return reason;
    }
}
