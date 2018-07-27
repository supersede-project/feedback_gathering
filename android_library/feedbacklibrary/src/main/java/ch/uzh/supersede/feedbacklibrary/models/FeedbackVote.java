package ch.uzh.supersede.feedbacklibrary.models;

public class FeedbackVote {
    private long id;
    private int vote;
    private String voterUsername;

    public FeedbackVote(int vote, String voterUsername) {
        this.vote = vote;
        this.voterUsername = voterUsername;
    }

    public int getVote() {
        return vote;
    }

    public String getVoterUsername() {
        return voterUsername;
    }
}

