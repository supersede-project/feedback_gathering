package ch.uzh.supersede.feedbacklibrary.models;

import java.util.Date;

public final class FeedbackResponse {
    private long id;
    private AndroidUser user;
    private String content;
    private Date createdAt;
    private Date updatedAt;

    public FeedbackResponse(AndroidUser user, String content) {
        this.user = user;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public AndroidUser getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}

