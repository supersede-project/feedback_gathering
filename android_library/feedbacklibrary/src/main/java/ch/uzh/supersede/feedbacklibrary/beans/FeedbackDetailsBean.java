package ch.uzh.supersede.feedbacklibrary.beans;

import java.io.Serializable;
import java.util.*;

import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS;

public class FeedbackDetailsBean implements Serializable{

    private UUID feedbackUid;
    private String title;
    private String description;
    private String userName;
    private String[] labels;
    private long timeStamp;
    private int upVotes;
    private FEEDBACK_STATUS feedbackStatus;
    private List<FeedbackResponseBean> feedbackResponses = new ArrayList<>();
    private FeedbackBean feedbackBean;

    private FeedbackDetailsBean() {
    }

    public static class Builder {
        private UUID feedbackUid;
        private String title;
        private String description;
        private String userName;
        private String[] labels;
        private long timeStamp;
        private int upVotes;
        private FEEDBACK_STATUS feedbackStatus;
        private List<FeedbackResponseBean> feedbackResponses = new ArrayList<>();
        private FeedbackBean feedbackBean;

        public Builder() {
            //NOP
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withLabels(String... labels) {
            this.labels = labels;
            return this;
        }

        public Builder withTimestamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder withUpVotes(int upVotes) {
            this.upVotes = upVotes;
            return this;
        }

        public Builder withStatus(FEEDBACK_STATUS feedbackStatus) {
            this.feedbackStatus = feedbackStatus;
            return this;
        }

        public Builder withResponses(List<FeedbackResponseBean> feedbackResponses) {
            this.feedbackResponses = feedbackResponses;
            return this;
        }

        public Builder withFeedbackBean(FeedbackBean feedbackBean) {
            this.feedbackBean = feedbackBean;
            return this;
        }

        public Builder withFeedbackUid(UUID feedbackUid) {
            this.feedbackUid = feedbackUid;
            return this;
        }

        public FeedbackDetailsBean build() {
            if (CompareUtility.notNull(feedbackUid,title,userName,timeStamp,description,feedbackStatus,feedbackBean)) {
                FeedbackDetailsBean bean = new FeedbackDetailsBean();
                bean.feedbackUid = feedbackUid;
                bean.title = this.title;
                bean.description = this.description;
                bean.userName = this.userName;
                bean.timeStamp = this.timeStamp;
                bean.upVotes = this.upVotes;
                bean.labels = labels;
                bean.feedbackStatus = this.feedbackStatus;
                bean.feedbackResponses = this.feedbackResponses;
                bean.feedbackBean = this.feedbackBean;
                return bean;
            }
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUserName() {
        return userName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public String getUpVotesAsText() {
        return upVotes <= 0 ? String.valueOf(upVotes) : "+" + upVotes;
    }

    public List<FeedbackResponseBean> getResponses() {
        return feedbackResponses;
    }

    public FEEDBACK_STATUS getFeedbackStatus() {
        return feedbackStatus;
    }

    public FeedbackBean getFeedbackBean() {
        return feedbackBean;
    }

    public String[] getLabels() {
        return labels;
    }

    public UUID getFeedbackUid() {
        return feedbackUid;
    }
}
