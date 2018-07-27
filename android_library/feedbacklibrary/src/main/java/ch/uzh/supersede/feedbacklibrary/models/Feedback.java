package ch.uzh.supersede.feedbacklibrary.models;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

import ch.uzh.supersede.feedbacklibrary.utils.Enums;

public class Feedback implements Serializable {

    @Expose
    @SerializedName("contextInformation")
    private ContextInformationFeedback contextInformationFeedback;
    @Expose
    private String title;
    @Expose
    private String userIdentification;
    @Expose
    @SerializedName("audioFeedbacks")
    private List<AudioFeedback> audioFeedbackList;
    @Expose
    @SerializedName("categoryFeedbacks")
    private List<LabelFeedback> labelFeedbackList;
    @Expose
    @SerializedName("ratingFeedbacks")
    private List<RatingFeedback> ratingFeedbackList;
    @Expose
    @SerializedName("screenshotFeedbacks")
    private List<ScreenshotFeedback> screenshotFeedbackList;
    @Expose
    @SerializedName("textFeedbacks")
    private List<TextFeedback> textFeedbackList;
    @SerializedName("attachmentFeedbacks")
    private List<AbstractFeedbackPart> attachmentFeedbackList;
    private long id;
    private long applicationId;
    private long configurationId;
    private String language;
    private Date createdAt;
    private Date updatedAt;
    @Expose
    private String[] tags;
    private int votes;
    private int minVotes;
    private int maxVotes;
    private Enums.FEEDBACK_STATUS feedbackStatus;
    private boolean isPublic;
    private List<FeedbackResponse> feedbackResponses;


    private Feedback() {
    }

    public String[] getTags() {
        return tags;
    }

    public static class Builder {
        private ContextInformationFeedback contextInformationFeedback;
        private String title;
        private String userIdentification;
        private List<AudioFeedback> audioFeedbackList;
        private List<LabelFeedback> categoryFeedbackList;
        private List<RatingFeedback> ratingFeedbackList;
        private List<ScreenshotFeedback> screenshotFeedbackList;
        private List<TextFeedback> textFeedbackList;
        private String[] tags;
        private Enums.FEEDBACK_STATUS feedbackStatus;
        private boolean isPublic;
        private List<FeedbackResponse> feedbackResponses;

        public Builder() {
            //NOP
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withContextInformation(Context context) {
            this.contextInformationFeedback = new ContextInformationFeedback.Builder(context)
                    .withAndroidVersion()
                    .withCountry()
                    .withLocalTime()
                    .withMetaData()
                    .withResolution()
                    .withTimeZone()
                    .build();
            return this;
        }

        public Builder withUserIdentification(String userIdentification) {
            this.userIdentification = userIdentification;
            return this;
        }

        public Builder withAudioFeedback(AudioFeedback audioFeedback) {
            this.audioFeedbackList = new ArrayList<>();
            if (audioFeedback != null && audioFeedback.getAudioPath() != null) {
                audioFeedbackList.add(audioFeedback);
            }
            return this;
        }

        public Builder withTags(String[] feedbackTags) {
            this.tags = feedbackTags;
            return this;
        }

        public Builder withLabelFeedback(LabelFeedback labelFeedback) {
            this.categoryFeedbackList = new ArrayList<>();
            if (labelFeedback != null) {
                categoryFeedbackList.add(labelFeedback);
            }
            return this;
        }

        public Builder withRatingFeedback(RatingFeedback ratingFeedback) {
            this.ratingFeedbackList = new ArrayList<>();
            if (ratingFeedback != null) {
                ratingFeedbackList.add(ratingFeedback);
            }
            return this;
        }

        public Builder withScreenshotFeedback(ScreenshotFeedback screenshotFeedback) {
            this.screenshotFeedbackList = new ArrayList<>();
            if (screenshotFeedback != null && screenshotFeedback.getSize() > 0) {
                screenshotFeedbackList.add(screenshotFeedback);
            }
            return this;
        }

        public Builder withTextFeedback(TextFeedback textFeedback) {
            this.textFeedbackList = new ArrayList<>();
            if (textFeedback != null) {
                textFeedbackList.add(textFeedback);
            }
            return this;
        }

        public Builder withFeedbackStatus(Enums.FEEDBACK_STATUS feedbackStatus) {
            this.feedbackStatus = feedbackStatus;
            return this;
        }

        public Builder withIsPublic(boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        public Builder withFeedbackResponses(List<FeedbackResponse> feedbackResponses){
            this.feedbackResponses = feedbackResponses;
            return this;
        }

        public Feedback build() {
            Feedback bean = new Feedback();
            bean.contextInformationFeedback = this.contextInformationFeedback;
            bean.title = this.title;
            bean.userIdentification = this.userIdentification;
            bean.audioFeedbackList = new ArrayList<>(this.audioFeedbackList);
            bean.labelFeedbackList = new ArrayList<>(this.categoryFeedbackList);
            bean.ratingFeedbackList = new ArrayList<>(this.ratingFeedbackList);
            bean.screenshotFeedbackList = new ArrayList<>(this.screenshotFeedbackList);
            bean.textFeedbackList = new ArrayList<>(this.textFeedbackList);
            bean.tags = tags;
            bean.createdAt = new Date();
            bean.feedbackStatus = this.feedbackStatus;
            bean.isPublic = this.isPublic;
            bean.feedbackResponses = this.feedbackResponses;
            return bean;
        }

    }

    public long getApplicationId() {
        return applicationId;
    }


    public long getConfigurationId() {
        return configurationId;
    }


    public ContextInformationFeedback getContextInformationFeedback() {
        return contextInformationFeedback;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public String getUserIdentification() {
        return userIdentification;
    }

    public List<AudioFeedback> getAudioFeedbackList() {
        return audioFeedbackList;
    }

    public List<LabelFeedback> getLabelFeedbackList() {
        return labelFeedbackList;
    }

    public List<RatingFeedback> getRatingFeedbackList() {
        return ratingFeedbackList;
    }

    public List<ScreenshotFeedback> getScreenshotFeedbackList() {
        return screenshotFeedbackList;
    }

    public List<TextFeedback> getTextFeedbackList() {
        return textFeedbackList;
    }

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public List<AbstractFeedbackPart> getAttachmentFeedbackList() {
        return attachmentFeedbackList;
    }

    public int getVotes() {
        return votes;
    }

    public int getMinVotes() {
        return minVotes;
    }

    public int getMaxVotes() {
        return maxVotes;
    }

    public Enums.FEEDBACK_STATUS getFeedbackStatus() {
        return feedbackStatus;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public List<FeedbackResponse> getFeedbackResponses() {
        return feedbackResponses;
    }
}
