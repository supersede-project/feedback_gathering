package ch.uzh.supersede.feedbacklibrary.feedback;

import android.os.Build;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.models.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.models.LabelFeedback;
import ch.uzh.supersede.feedbacklibrary.models.RatingFeedback;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.TextFeedback;
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
    private List<AudioFeedback> audioFeedbackList;
    @Expose
    private List<LabelFeedback> categoryFeedbackList;
    @Expose
    private List<RatingFeedback> ratingFeedbackList;
    @Expose
    private List<ScreenshotFeedback> screenshotFeedbackList;
    @Expose
    private List<TextFeedback> textFeedbackList;

    private Feedback() {
    }

    public static class Builder {
        private long applicationId;
        private long configurationId;
        private Map<String, Object> contextInformation;
        private String language;
        private String title;
        private String userIdentification;
        private List<AudioFeedback> audioFeedbackList;
        private List<LabelFeedback> categoryFeedbackList;
        private List<RatingFeedback> ratingFeedbackList;
        private List<ScreenshotFeedback> screenshotFeedbackList;
        private List<TextFeedback> textFeedbackList;

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

        public Builder withAudioMechanism(AudioFeedback audioFeedback) {
            this.audioFeedbackList = new ArrayList<>();
            if (audioFeedback.getAudioPath() != null) {
                audioFeedbackList.add(audioFeedback);
            }
            return this;
        }

        public Builder withCategoryMechanism(LabelFeedback labelFeedback) {
            this.categoryFeedbackList = new ArrayList<>();
            categoryFeedbackList.add(labelFeedback);
            return this;
        }

        public Builder withRatingMechanism(RatingFeedback ratingFeedback) {
            this.ratingFeedbackList = new ArrayList<>();
            ratingFeedbackList.add(ratingFeedback);
            return this;
        }

        public Builder withScreenshotMechanism(ScreenshotFeedback screenshotFeedback) {
            this.screenshotFeedbackList = new ArrayList<>();
            if (screenshotFeedback.getImagePath() != null) {
                screenshotFeedbackList.add(screenshotFeedback);
            }
            return this;
        }

        public Builder withTextMechanism(TextFeedback textFeedback) {
            this.textFeedbackList = new ArrayList<>();
            textFeedbackList.add(textFeedback);
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

    public List<AudioFeedback> getAudioFeedbackList() {
        return audioFeedbackList;
    }

    public List<LabelFeedback> getCategoryFeedbackList() {
        return categoryFeedbackList;
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

}
