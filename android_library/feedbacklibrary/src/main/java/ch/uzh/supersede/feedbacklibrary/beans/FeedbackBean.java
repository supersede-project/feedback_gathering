package ch.uzh.supersede.feedbacklibrary.beans;

import android.content.Context;

import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.stubs.GeneratorStub;
import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public final class FeedbackBean implements Serializable {

    private long feedbackId;
    private String title;
    private String[] tags;
    private String userName;
    private long timeStamp;
    private int upVotes;
    private int maxUpVotes;
    private int minUpVotes;
    private int responses;
    private FEEDBACK_STATUS feedbackStatus;
    private boolean isPublic;
    private String bitmapName;

    private FeedbackBean() {
    }

    public static class Builder {
        private long feedbackId;
        private String title;
        private String[] tags;
        private String userName;
        private long timeStamp;
        private int upVotes;
        private int maxUpVotes;
        private int minUpVotes;
        private int responses;
        private FEEDBACK_STATUS feedbackStatus;
        private boolean isPublic;

        public Builder() {
            //NOP
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withTags(String[] tags) {
            this.tags = tags;
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

        public Builder withFeedbackId(long feedbackId) {
            this.feedbackId = feedbackId;
            return this;
        }

        public Builder isPublic(boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }


        public FeedbackBean build() {
            if (CompareUtility.notNull(feedbackId, title, timeStamp, feedbackStatus)) {
                FeedbackBean bean = new FeedbackBean();
                bean.feedbackId = feedbackId;
                bean.title = this.title;
                bean.tags = this.tags;
                bean.userName = this.userName;
                bean.timeStamp = this.timeStamp;
                bean.upVotes = this.upVotes;
                bean.maxUpVotes = this.maxUpVotes;
                bean.minUpVotes = this.minUpVotes;
                bean.responses = this.responses;
                bean.feedbackStatus = this.feedbackStatus;
                bean.isPublic = this.isPublic;
                return bean;
            }
            return null;
        }

        //Showcase
        public FeedbackBean fromLocalFeedbackBean(Context context, LocalFeedbackBean localFeedbackBean) {
            FeedbackBean bean = new FeedbackBean();
            bean.feedbackId = localFeedbackBean.getFeedbackId();
            bean.title = localFeedbackBean.getTitle();
            bean.tags = FeedbackDatabase.getInstance(context).readTags(localFeedbackBean.getFeedbackId());
            bean.userName = localFeedbackBean.getOwner() == 1 ? FeedbackDatabase.getInstance(context).readString(USER_NAME, null) : GeneratorStub.BagOfNames.pickRandom();
            bean.timeStamp = localFeedbackBean.getCreationDate();
            bean.upVotes = localFeedbackBean.getVotes();
            bean.maxUpVotes = 50; //FIXME mbo
            bean.minUpVotes = -30; //FIXME mbo
            bean.responses = localFeedbackBean.getResponses();
            bean.feedbackStatus = localFeedbackBean.getFeedbackStatus();
            bean.isPublic = false;
            return bean;
        }
    }

    public String getTitle() {
        return title;
    }

    public String[] getTags() {
        return tags;
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

    public String getVotesAsText() {
        return upVotes <= 0 ? String.valueOf(upVotes) : "+" + upVotes;
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

    public long getFeedbackId() {
        return feedbackId;
    }

    public String downVote() {
        upVotes--;
        return getVotesAsText();
    }

    public String upVote() {
        upVotes++;
        return getVotesAsText();
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isOwnFeedback(Context context) {
        return ACTIVE.check(context, false) && getUserName().equals(FeedbackDatabase.getInstance(context).readString(USER_NAME, ""));
    }

    public String getBitmapName() {
        return bitmapName;
    }
}
