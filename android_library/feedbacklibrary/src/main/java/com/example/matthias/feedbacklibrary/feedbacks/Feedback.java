package com.example.matthias.feedbacklibrary.feedbacks;

import android.os.Build;
import android.util.Log;

import com.example.matthias.feedbacklibrary.models.AttachmentMechanism;
import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.example.matthias.feedbacklibrary.models.CategoryMechanism;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.RatingMechanism;
import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Feedback
 */
public class Feedback implements Serializable {
    // Application
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
    // Mechanisms
    @Expose
    private List<AttachmentFeedback> attachmentFeedbacks = new ArrayList<>();
    @Expose
    private List<AudioFeedback> audioFeedbacks = new ArrayList<>();
    @Expose
    private List<HashMap<String, Object>> categoryFeedbacks = new ArrayList<>();
    @Expose
    private List<RatingFeedback> ratingFeedbacks = new ArrayList<>();
    @Expose
    private List<ScreenshotFeedback> screenshotFeedbacks = new ArrayList<>();
    @Expose
    private List<TextFeedback> textFeedbacks = new ArrayList<>();

    public Feedback(List<Mechanism> allMechanisms) {
        for (Mechanism mechanism : allMechanisms) {
            if (mechanism.isActive()) {
                String type = mechanism.getType();
                switch (type) {
                    case Mechanism.ATTACHMENT_TYPE:
                        AttachmentMechanism attachmentMechanism = (AttachmentMechanism) mechanism;
                        break;
                    case Mechanism.AUDIO_TYPE:
                        AudioMechanism audioMechanism = (AudioMechanism) mechanism;
                        if (audioMechanism.getAudioPath() != null) {
                            audioFeedbacks.add(new AudioFeedback(audioMechanism, audioFeedbacks.size()));
                        }
                        break;
                    case Mechanism.CATEGORY_TYPE:
                        CategoryFeedback categoryFeedback = new CategoryFeedback((CategoryMechanism) mechanism);
                        categoryFeedbacks.addAll(categoryFeedback.getCategories());
                        break;
                    case Mechanism.RATING_TYPE:
                        ratingFeedbacks.add(new RatingFeedback((RatingMechanism) mechanism));
                        break;
                    case Mechanism.SCREENSHOT_TYPE:
                        ScreenshotMechanism screenshotMechanism = (ScreenshotMechanism) mechanism;
                        if (screenshotMechanism.getImagePath() != null) {
                            screenshotFeedbacks.add(new ScreenshotFeedback(screenshotMechanism, screenshotFeedbacks.size()));
                        }
                        break;
                    case Mechanism.TEXT_TYPE:
                        textFeedbacks.add(new TextFeedback((TextMechanism) mechanism));
                        break;
                    default:
                        Log.wtf("Feedback", "Unknown mechanism type '" + type + "'");
                        break;
                }

            }
        }
    }

    public long getApplicationId() {
        return applicationId;
    }

    public List<AttachmentFeedback> getAttachmentFeedbacks() {
        return attachmentFeedbacks;
    }

    public List<AudioFeedback> getAudioFeedbacks() {
        return audioFeedbacks;
    }

    public List<HashMap<String, Object>> getCategoryFeedbacks() {
        return categoryFeedbacks;
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

    public List<RatingFeedback> getRatingFeedbacks() {
        return ratingFeedbacks;
    }

    public List<ScreenshotFeedback> getScreenshotFeedbacks() {
        return screenshotFeedbacks;
    }

    public List<TextFeedback> getTextFeedbacks() {
        return textFeedbacks;
    }

    public String getTitle() {
        return title;
    }

    public String getUserIdentification() {
        return userIdentification;
    }

    public void initContextInformation() {
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

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public void setAttachmentFeedbacks(List<AttachmentFeedback> attachmentFeedbacks) {
        this.attachmentFeedbacks = attachmentFeedbacks;
    }

    public void setAudioFeedbacks(List<AudioFeedback> audioFeedbacks) {
        this.audioFeedbacks = audioFeedbacks;
    }

    public void setCategoryFeedbacks(List<HashMap<String, Object>> categoryFeedbacks) {
        this.categoryFeedbacks = categoryFeedbacks;
    }

    public void setConfigurationId(long configurationId) {
        this.configurationId = configurationId;
    }

    public void setContextInformation(Map<String, Object> contextInformation) {
        this.contextInformation = contextInformation;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRatingFeedbacks(List<RatingFeedback> ratingFeedbacks) {
        this.ratingFeedbacks = ratingFeedbacks;
    }

    public void setScreenshotFeedbacks(List<ScreenshotFeedback> screenshotFeedbacks) {
        this.screenshotFeedbacks = screenshotFeedbacks;
    }

    public void setTextFeedbacks(List<TextFeedback> textFeedbacks) {
        this.textFeedbacks = textFeedbacks;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserIdentification(String userIdentification) {
        this.userIdentification = userIdentification;
    }

}
