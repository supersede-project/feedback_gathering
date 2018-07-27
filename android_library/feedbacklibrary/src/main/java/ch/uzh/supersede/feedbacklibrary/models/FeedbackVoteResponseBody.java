package ch.uzh.supersede.feedbacklibrary.models;

public class FeedbackVoteResponseBody {
    private int id;
    private int vote;

    public FeedbackVoteResponseBody(int vote) {
        this.vote = vote;
    }

    public int getId() {
        return id;
    }

    public int getVote() {
        return vote;
    }

}

