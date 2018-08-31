package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.SerializedName;

public final class AndroidUser {
    private long id;
    private String name;
    private long applicationId;
    private int karma;
    private VoteCount voteCount;
    private int feedbackCount;
    @SerializedName("developer")
    private boolean isDeveloper;
    @SerializedName("blocked")
    private boolean isBlocked;

    public AndroidUser(String name) {
        this.name = name;
    }

    public AndroidUser(String name, boolean isDeveloper) {
        this.name = name;
        this.isDeveloper = isDeveloper;
    }

    public AndroidUser(String name, boolean isDeveloper, int karma, boolean isBlocked) {
        this.name = name;
        this.isDeveloper = isDeveloper;
        this.karma = karma;
        this.isBlocked = isBlocked;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public int getKarma() {
        return karma;
    }

    public VoteCount getVoteCount() {
        return voteCount;
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public class VoteCount {
        int upVoteCount;
        int downVoteCount;

        public int getUpVoteCount() {
            return upVoteCount;
        }

        public int getDownVoteCount() {
            return downVoteCount;
        }
    }
}
