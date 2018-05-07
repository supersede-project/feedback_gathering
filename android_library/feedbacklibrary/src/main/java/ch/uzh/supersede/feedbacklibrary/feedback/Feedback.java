package ch.uzh.supersede.feedbacklibrary.feedback;

import android.os.Build;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.models.AttachmentMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;
import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

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
    private List<AttachmentMechanism> attachmentFeedbackList;
    @Expose
    private List<AudioMechanism> audioFeedbackList;
    @Expose
    private List<CategoryMechanism> categoryFeedbackList;
    @Expose
    private List<RatingMechanism> ratingFeedbackList;
    @Expose
    private List<ScreenshotMechanism> screenshotFeedbackList;
    @Expose
    private List<TextMechanism> textFeedbackList;

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

        public Builder withContextInformation() {
            this.contextInformation = createContextInformation();
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
            if (attachmentMechanism != null && attachmentMechanism.getAttachmentPaths() != null) {
                attachmentFeedbackList.add(attachmentMechanism);
            }
            return this;
        }

        public Builder withAudioMechanism(AudioMechanism audioMechanism) {
            this.audioFeedbackList = new ArrayList<>();
            if (audioMechanism.getAudioPath() != null) {
                audioFeedbackList.add(audioMechanism);
            }
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
            if (screenshotMechanism.getImagePath() != null) {
                screenshotFeedbackList.add(screenshotMechanism);
            }
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

        private static Map<String, Object> createContextInformation() {
            Map<String, Object> deviceInfo = new HashMap<>();
            deviceInfo.put("release", Build.VERSION.RELEASE);
            deviceInfo.put("device", Build.DEVICE);
            deviceInfo.put("model", Build.MODEL);
            deviceInfo.put("product", Build.PRODUCT);
            deviceInfo.put("brand", Build.BRAND);
            deviceInfo.put("display", Build.DISPLAY);
            deviceInfo.put("androidId", Build.ID);
            deviceInfo.put("manufacturer", Build.MANUFACTURER);
            deviceInfo.put("serial", Build.SERIAL);
            deviceInfo.put("host", Build.HOST);
            return deviceInfo;
        }
    }

    public long getApplicationId() {
        return applicationId;
    }


    public long getConfigurationId() {
        return configurationId;
    }


    public Map<String, Object> getContextInformation() {
        return contextInformation;
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

    public List<AttachmentMechanism> getAttachmentFeedbackList() {
        return attachmentFeedbackList;
    }

    public List<AudioMechanism> getAudioFeedbackList() {
        return audioFeedbackList;
    }

    public List<CategoryMechanism> getCategoryFeedbackList() {
        return categoryFeedbackList;
    }

    public List<RatingMechanism> getRatingFeedbackList() {
        return ratingFeedbackList;
    }

    public List<ScreenshotMechanism> getScreenshotFeedbackList() {
        return screenshotFeedbackList;
    }

    public List<TextMechanism> getTextFeedbackList() {
        return textFeedbackList;
    }

}
