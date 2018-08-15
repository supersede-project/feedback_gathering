package ch.uzh.supersede.feedbacklibrary.models;

public class FeedbackVoteRequestBody {
    private long id;
    private int vote;
    private String voterUsername;

    public FeedbackVoteRequestBody(int vote, String voterUsername) {
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

