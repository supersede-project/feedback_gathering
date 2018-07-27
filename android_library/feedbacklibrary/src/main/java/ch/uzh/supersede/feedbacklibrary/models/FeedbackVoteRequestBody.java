package ch.uzh.supersede.feedbacklibrary.models;

public class FeedbackVoteRequestBody {
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

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setVoterUsername(String voterUsername) {
        this.voterUsername = voterUsername;
    }
}

