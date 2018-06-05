package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.SerializedName;

public class AndroidUser {
    private long id;
    private String name;
    private long applicationId;
    private int karma;
    private int voteCount;
    private int feedbackCount;
    @SerializedName("developer")
    private boolean isDeveloper;
    @SerializedName("blocked")
    private boolean isBlocked;

    public AndroidUser(String name, boolean isDeveloper) {
        this.name = name;
        this.isDeveloper = isDeveloper;
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

    public int getVoteCount() {
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
}
