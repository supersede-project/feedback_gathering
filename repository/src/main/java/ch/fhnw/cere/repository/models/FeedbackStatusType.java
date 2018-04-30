package ch.fhnw.cere.repository.models;

public enum FeedbackStatusType {

    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    REJECTED("Rejected"),
    DUPLICATE("Duplicate"),
    CLOSED("Closed");

    private String feedbacktype;

    FeedbackStatusType(String feedbacktype) {
        this.feedbacktype = feedbacktype;
    }

    public String getFeedbacktype() {
        return feedbacktype;
    }

}
