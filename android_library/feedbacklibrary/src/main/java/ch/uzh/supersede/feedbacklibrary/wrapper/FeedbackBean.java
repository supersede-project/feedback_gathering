package ch.uzh.supersede.feedbacklibrary.wrapper;

import android.graphics.Color;

import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

public class FeedbackBean implements Serializable{

    @SuppressWarnings("squid:UnusedPrivateMethod")
    public enum FEEDBACK_STATUS {
        OPEN("Open", Color.rgb(0, 150, 255)),
        IN_PROGRESS("In Progress", Color.rgb(222, 222, 0)),
        REJECTED("Rejected", Color.rgb(255, 150, 0)),
        DUPLICATE("Duplicate", Color.rgb(150, 150, 150)),
        CLOSED("Closed", Color.rgb(0, 222, 100));

        FEEDBACK_STATUS(String label, int color) {
            this.label = label;
            this.color = color;
        }

        private int color;
        private String label;

        public int getColor() {
            return color;
        }

        public String getLabel() {
            return label;
        }
    }

    private String title;
    private String userName;
    private String technicalUserName;
    private long timeStamp;
    private int upVotes;
    private int maxUpVotes;
    private int minUpVotes;
    private int responses;
    private FEEDBACK_STATUS feedbackStatus;

    private FeedbackBean() {
    }

    public static class Builder {
        private String title;
        private String userName;
        private String technicalUserName;
        private long timeStamp;
        private int upVotes;
        private int maxUpVotes;
        private int minUpVotes;
        private int responses;
        private FEEDBACK_STATUS feedbackStatus;

        public Builder() {
            //NOP
        }

        public Builder withTitle(String title) {
            this.title = title;
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

        public Builder withUpVotes(int upVotes) {
            this.upVotes = upVotes;
            return this;
        }

        public Builder withMaxUpVotes(int maxUpVotes) {
            this.maxUpVotes = maxUpVotes;
            return this;
        }
        public Builder withMinUpVotes(int minUpVotes) {
            this.minUpVotes = minUpVotes;
            return this;
        }

        public Builder withResponses(int responses) {
            this.responses = responses;
            return this;
        }

        public Builder withStatus(FEEDBACK_STATUS feedbackStatus) {
            this.feedbackStatus = feedbackStatus;
            return this;
        }

        public FeedbackBean build() {
            if (CompareUtility.notNull(title,userName,technicalUserName,timeStamp,maxUpVotes, minUpVotes,feedbackStatus)) {
                FeedbackBean bean = new FeedbackBean();
                bean.title = this.title;
                bean.userName = this.userName;
                bean.technicalUserName = this.technicalUserName;
                bean.timeStamp = this.timeStamp;
                bean.upVotes = this.upVotes;
                bean.maxUpVotes = this.maxUpVotes;
                bean.minUpVotes = this.minUpVotes;
                bean.responses = this.responses;
                bean.feedbackStatus = this.feedbackStatus;
                return bean;
            }
            return null;
        }
    }

    public String getTitle() {
        return title;
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

    public int getUpVotes() {
        return upVotes;
    }

    public String getUpVotesAsText() {
        return upVotes<=0?String.valueOf(upVotes):"+"+upVotes;
    }

    public int getResponses() {
        return responses;
    }

    public FEEDBACK_STATUS getFeedbackStatus() {
        return feedbackStatus;
    }

    public int getMaxUpVotes() {
        return maxUpVotes;
    }

    public int getMinUpVotes() {
        return minUpVotes;
    }
}
