package ch.uzh.supersede.feedbacklibrary.feedback;

import android.os.Build;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AttachmentMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;
import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;

public class Feedback implements Serializable {
    @Expose
    private long applicationId;
    @Expose
    private long configurationId;
    @Expose
    private Map<String, Object> contextInformation;
    @Expose
    private String language;
    @Expose
    private String title;
    @Expose
    private String userIdentification;
    @Expose
    private List<AttachmentMechanism> attachmentFeedbackList = new ArrayList<>();
    @Expose
    private List<AudioMechanism> audioFeedbackList = new ArrayList<>();
    @Expose
    private List<CategoryMechanism> categoryFeedbackList = new ArrayList<>();
    @Expose
    private List<RatingMechanism> ratingFeedbackList = new ArrayList<>();
    @Expose
    private List<ScreenshotMechanism> screenshotFeedbackList = new ArrayList<>();
    @Expose
    private List<TextMechanism> textFeedbackList = new ArrayList<>();

    private Feedback() {
    }

    public static class Builder {
        private long applicationId;
        private long configurationId;
        private Map<String, Object> contextInformation;
        private String language;
        private String title;
        private String userIdentification;
        private List<AttachmentMechanism> attachmentFeedbackList;
        private List<AudioMechanism> audioFeedbackList;
        private List<CategoryMechanism> categoryFeedbackList;
        private List<RatingMechanism> ratingFeedbackList;
        private List<ScreenshotMechanism> screenshotFeedbackList;
        private List<TextMechanism> textFeedbackList;

        public Builder() {
            //NOP
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withApplicationId(long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder withConfigurationId(long configurationId) {
            this.configurationId = configurationId;
            return this;
        }

        public Builder withContextInformation(Map<String, Object> contextInformation) {
            this.contextInformation = new HashMap<>(contextInformation);
            return this;
        }

        public Builder withLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder withUserIdentification(String userIdentification) {
            this.userIdentification = userIdentification;
            return this;
        }

        public Builder withAttachmentMechanism(AttachmentMechanism attachmentMechanism) {
            this.attachmentFeedbackList = new ArrayList<>();
            attachmentFeedbackList.add(attachmentMechanism);
            return this;
        }

        public Builder withAudioMechanism(AudioMechanism audioMechanism) {
            this.audioFeedbackList = new ArrayList<>();
            audioFeedbackList.add(audioMechanism);
            return this;
        }

        public Builder withCategoryMechanism(CategoryMechanism categoryMechanism) {
            this.categoryFeedbackList = new ArrayList<>();
            categoryFeedbackList.add(categoryMechanism);
            return this;
        }

        public Builder withRatingMechanism(RatingMechanism ratingMechanism) {
            this.ratingFeedbackList = new ArrayList<>();
            ratingFeedbackList.add(ratingMechanism);
            return this;
        }

        public Builder withScreenshotMechanism(ScreenshotMechanism screenshotMechanism) {
            this.screenshotFeedbackList = new ArrayList<>();
            screenshotFeedbackList.add(screenshotMechanism);
            return this;
        }

        public Builder withTextMechanism(TextMechanism textMechanism) {
            this.textFeedbackList = new ArrayList<>();
            textFeedbackList.add(textMechanism);
            return this;
        }

        public Feedback build() {
            if (CompareUtility.notNull(title, userIdentification, applicationId)) {
                Feedback bean = new Feedback();
                bean.applicationId = applicationId;
                bean.configurationId = this.configurationId;
                bean.contextInformation = this.contextInformation;
                bean.language = this.language;
                bean.title = this.title;
                bean.userIdentification = this.userIdentification;
                bean.attachmentFeedbackList = new ArrayList<>(this.attachmentFeedbackList);
                bean.audioFeedbackList = new ArrayList<>(this.audioFeedbackList);
                bean.categoryFeedbackList = new ArrayList<>(this.categoryFeedbackList);
                bean.ratingFeedbackList = new ArrayList<>(this.ratingFeedbackList);
                bean.screenshotFeedbackList = new ArrayList<>(this.screenshotFeedbackList);
                bean.textFeedbackList = new ArrayList<>(this.textFeedbackList);
                return bean;
            }
            return null;
        }
    }

    public Feedback(List<AbstractMechanism> allMechanisms) {
        for (AbstractMechanism mechanism : allMechanisms) {
            if (mechanism != null && mechanism.isActive()) {
                String type = mechanism.getType();
                switch (type) {
                    case ATTACHMENT_TYPE:
                        AttachmentMechanism attachmentMechanism = (AttachmentMechanism) mechanism;
                        break;
                    case AUDIO_TYPE:
                        AudioMechanism audioMechanism = (AudioMechanism) mechanism;
                        if (audioMechanism.getAudioPath() != null) {
                            audioFeedbackList.add(audioMechanism);
                        }
                        break;
                    case CATEGORY_TYPE:
                        categoryFeedbackList.add((CategoryMechanism) mechanism);
                        break;
                    case RATING_TYPE:
                        ratingFeedbackList.add((RatingMechanism) mechanism);
                        break;
                    case SCREENSHOT_TYPE:
                        ScreenshotMechanism screenshotMechanism = (ScreenshotMechanism) mechanism;
                        if (screenshotMechanism.getImagePath() != null) {
                            screenshotFeedbackList.add(screenshotMechanism);
                        }
                        break;
                    case TEXT_TYPE:
                        textFeedbackList.add((TextMechanism) mechanism);
                        break;
                    default:
                        Log.wtf("Feedback", "Unknown mechanism type '" + type + "'");
                        break;
                }

            }
        }
    }

    public void initContextInformation() {
        // TODO: not yet sent to the repository, i.e., needs to be implemented
        contextInformation = new HashMap<>();
        contextInformation.put("release", Build.VERSION.RELEASE);
        contextInformation.put("device", Build.DEVICE);
        contextInformation.put("model", Build.MODEL);
        contextInformation.put("product", Build.PRODUCT);
        contextInformation.put("brand", Build.BRAND);
        contextInformation.put("display", Build.DISPLAY);
        contextInformation.put("androidId", Build.ID);
        contextInformation.put("manufacturer", Build.MANUFACTURER);
        contextInformation.put("serial", Build.SERIAL);
        contextInformation.put("host", Build.HOST);
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(long configurationId) {
        this.configurationId = configurationId;
    }

    public Map<String, Object> getContextInformation() {
        return contextInformation;
    }

    public void setContextInformation(Map<String, Object> contextInformation) {
        this.contextInformation = contextInformation;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserIdentification() {
        return userIdentification;
    }

    public void setUserIdentification(String userIdentification) {
        this.userIdentification = userIdentification;
    }

    public List<AttachmentMechanism> getAttachmentFeedbackList() {
        return attachmentFeedbackList;
    }

    public void setAttachmentFeedbackList(List<AttachmentMechanism> attachmentFeedbackList) {
        this.attachmentFeedbackList = attachmentFeedbackList;
    }

    public List<AudioMechanism> getAudioFeedbackList() {
        return audioFeedbackList;
    }

    public void setAudioFeedbackList(List<AudioMechanism> audioFeedbackList) {
        this.audioFeedbackList = audioFeedbackList;
    }

    public List<CategoryMechanism> getCategoryFeedbackList() {
        return categoryFeedbackList;
    }

    public void setCategoryFeedbackList(List<CategoryMechanism> categoryFeedbackList) {
        this.categoryFeedbackList = categoryFeedbackList;
    }

    public List<RatingMechanism> getRatingFeedbackList() {
        return ratingFeedbackList;
    }

    public void setRatingFeedbackList(List<RatingMechanism> ratingFeedbackList) {
        this.ratingFeedbackList = ratingFeedbackList;
    }

    public List<ScreenshotMechanism> getScreenshotFeedbackList() {
        return screenshotFeedbackList;
    }

    public void setScreenshotFeedbackList(List<ScreenshotMechanism> screenshotFeedbackList) {
        this.screenshotFeedbackList = screenshotFeedbackList;
    }

    public List<TextMechanism> getTextFeedbackList() {
        return textFeedbackList;
    }

    public void setTextFeedbackList(List<TextMechanism> textFeedbackList) {
        this.textFeedbackList = textFeedbackList;
    }
}
