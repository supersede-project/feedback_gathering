package ch.uzh.supersede.feedbacklibrary.beans;

import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

public final class FeedbackResponseBean implements Serializable {

    private long responseId;
    private long feedbackId;
    private String userName;
    private String content;
    private long timeStamp;
    private boolean isFeedbackOwner;
    private boolean isDeveloper;

    private FeedbackResponseBean() {
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isFeedbackOwner() {
        return isFeedbackOwner;
    }

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public long getFeedbackId() {
        return feedbackId;
    }

    public long getResponseId() {
        return responseId;
    }

    public static class Builder {
        private long responseId;
        private long feedbackId;
        private String userName;
        private String content;
        private long timeStamp;
        private boolean isFeedbackOwner;
        private boolean isDeveloper;

        public Builder() {
            //NOP
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withTimestamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder isFeedbackOwner(boolean isFeedbackOwner) {
            this.isFeedbackOwner = isFeedbackOwner;
            return this;
        }

        public Builder isDeveloper(boolean isDeveloper) {
            this.isDeveloper = isDeveloper;
            return this;
        }

        public Builder withFeedbackId(long feedbackId) {
            this.feedbackId = feedbackId;
            return this;
        }

        public Builder withResponseId(long responseId) {
            this.responseId = responseId;
            return this;
        }

        public FeedbackResponseBean build() {
            if (CompareUtility.notNull(feedbackId, content, userName, timeStamp)) {
                FeedbackResponseBean bean = new FeedbackResponseBean();
                bean.feedbackId = feedbackId;
                bean.content = this.content;
                bean.userName = this.userName;
                bean.timeStamp = this.timeStamp;
                bean.isFeedbackOwner = this.isFeedbackOwner;
                bean.isDeveloper = this.isDeveloper;
                bean.responseId = this.responseId;
                return bean;
            }
            return null;
        }
    }
}
