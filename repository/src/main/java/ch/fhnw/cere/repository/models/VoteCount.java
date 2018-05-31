package ch.fhnw.cere.repository.models;

public class VoteCount {

    private int upVoteCount = 0;

    private int downVoteCount = 0;

    public VoteCount() {
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(int downVoteCount) {
        this.downVoteCount = downVoteCount;
    }
}
