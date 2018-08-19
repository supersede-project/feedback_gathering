package ch.uzh.supersede.feedbacklibrary.beans;

import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

public final class FeedbackVoteBean implements Serializable {
    private int vote;
    private String userName;
    private String technicalUserName;
    private long timeStamp;
    private String feedbackId;

    private FeedbackVoteBean() {
    }

    public int getVote() {
        return vote;
    }

    public String getUserName() {
        return userName;
    }

    public String getTechnicalUserName() {
        return technicalUserName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public static class Builder {
        private int vote;
        private String userName;
        private String technicalUserName;
        private long timeStamp;
        private String feedbackId;

        public Builder() {
            //NOP
        }

        public Builder withVote(int vote) {
            this.vote = vote;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withTechnicalUserName(String technicalUserName) {
            this.technicalUserName = technicalUserName;
            return this;
        }

        public Builder withTimestamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder withFeedbackId(String feedbackId) {
            this.feedbackId = feedbackId;
            return this;
        }

        public FeedbackVoteBean build() {
            if (CompareUtility.notNull(vote, userName, technicalUserName, timeStamp, feedbackId)) {
                FeedbackVoteBean bean = new FeedbackVoteBean();
                bean.vote = this.vote;
                bean.userName = this.userName;
                bean.technicalUserName = this.technicalUserName;
                bean.timeStamp = this.timeStamp;
                bean.feedbackId = this.feedbackId;
                return bean;
            }
            return null;
        }
    }
}
