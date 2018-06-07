package ch.uzh.supersede.feedbacklibrary.models;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.format.Time;
import android.view.WindowManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        public Builder withContextInformation(Context context) {
            this.contextInformation = createContextInformation(context);
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

        public Builder withAudioFeedback(AudioFeedback audioFeedback) {
            this.audioFeedbackList = new ArrayList<>();
            if (audioFeedback != null && audioFeedback.getAudioPath() != null) {
                audioFeedbackList.add(audioFeedback);
            }
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
            if (screenshotFeedback != null && screenshotFeedback.getImagePath() != null) {
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
                bean.labelFeedbackList = new ArrayList<>(this.categoryFeedbackList);
                bean.ratingFeedbackList = new ArrayList<>(this.ratingFeedbackList);
                bean.screenshotFeedbackList = new ArrayList<>(this.screenshotFeedbackList);
                bean.textFeedbackList = new ArrayList<>(this.textFeedbackList);
                return bean;
            }
            return null;
        }

        private static Map<String, Object> createContextInformation(Context context) {
            Map<String, Object> deviceInfo = new HashMap<>();
            deviceInfo.put("androidVersion", Build.VERSION.RELEASE);
            deviceInfo.put("country", Locale.getDefault().getCountry());
            deviceInfo.put("metaData", Build.MODEL);

            deviceInfo.put("localTime", Calendar.getInstance().getTime());
            deviceInfo.put("timeZone", Time.getCurrentTimezone());

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                Point screen = new Point();
                windowManager.getDefaultDisplay().getRealSize(screen);
                deviceInfo.put("resolution", screen.x + "x" + screen.y);
            }

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

}
