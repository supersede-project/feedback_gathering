package ch.uzh.supersede.feedbacklibrary.models;

public class FeedbackVote {
    private int id;
    private int vote;

    public FeedbackVote(int vote) {
        this.vote = vote;
    }

    public int getId() {
        return id;
    }

    public int getVote() {
        return vote;
    }

}