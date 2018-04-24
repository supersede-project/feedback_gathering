package ch.uzh.supersede.feedbacklibrary.beans;

import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.utils.*;

public class FeedbackResponseBean implements Serializable{

    private String userName;
    private String technicalUserName;
    private String content;
    private long timeStamp;
    private boolean isFeedbackOwner;
    private boolean isDeveloper;

    private FeedbackResponseBean() {
    }

    public static class Builder {
        private String userName;
        private String technicalUserName;
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

        public Builder withTechnicalUserName(String technicalUserName) {
            this.technicalUserName = technicalUserName;
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

        public FeedbackResponseBean build() {
            if (CompareUtility.notNull(content,userName,technicalUserName,timeStamp)) {
                FeedbackResponseBean bean = new FeedbackResponseBean();
                bean.content = this.content;
                bean.userName = this.userName;
                bean.technicalUserName = this.technicalUserName;
                bean.timeStamp = this.timeStamp;
                bean.isFeedbackOwner = this.isFeedbackOwner;
                bean.isDeveloper = this.isDeveloper;
                return bean;
            }
            return null;
        }
    }

    public String getContent() {
        return content;
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

    public boolean isFeedbackOwner() {
        return isFeedbackOwner;
    }
    public boolean isDeveloper() {
        return isDeveloper;
    }
}
