package com.example.matthias.feedbacklibrary.feedbacks;

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
import java.util.List;

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
    private String language;
    @Expose
    private String title;
    @Expose
    private String userIdentification;
    // Mechanisms
    @Expose
    private List<AttachmentFeedback> attachmentFeedbacks = null;
    @Expose
    private List<AudioFeedback> audioFeedbacks = null;
    @Expose
    private List<CategoryFeedback> categoryFeedbacks = null;
    @Expose
    private List<RatingFeedback> ratingFeedbacks = null;
    @Expose
    private List<ScreenshotFeedback> screenshotFeedbacks = null;
    @Expose
    private List<TextFeedback> textFeedbacks = null;
    public Feedback(List<Mechanism> allMechanisms) {
        for (Mechanism mechanism : allMechanisms) {
            if (mechanism.isActive()) {
                String type = mechanism.getType();
                switch (type) {
                    case Mechanism.ATTACHMENT_TYPE:
                        // TODO: Implement attachment mechanism
                        if (attachmentFeedbacks == null) {
                            attachmentFeedbacks = new ArrayList<>();
                        }
                        AttachmentMechanism attachmentMechanism = (AttachmentMechanism) mechanism;
                        break;
                    case Mechanism.AUDIO_TYPE:
                        // TODO: Implement audio mechanism
                        AudioMechanism audioMechanism = (AudioMechanism) mechanism;
                        if (audioMechanism.getAudioPath() != null) {
                            if (audioFeedbacks == null) {
                                audioFeedbacks = new ArrayList<>();
                            }
                            audioFeedbacks.add(new AudioFeedback(audioMechanism, audioFeedbacks.size()));
                        }
                        break;
                    case Mechanism.CATEGORY_TYPE:
                        // TODO: Implement category mechanism
                        if (categoryFeedbacks == null) {
                            categoryFeedbacks = new ArrayList<>();
                        }
                        categoryFeedbacks.add(new CategoryFeedback((CategoryMechanism) mechanism));
                        break;
                    case Mechanism.RATING_TYPE:
                        if (ratingFeedbacks == null) {
                            ratingFeedbacks = new ArrayList<>();
                        }
                        ratingFeedbacks.add(new RatingFeedback((RatingMechanism) mechanism));
                        break;
                    case Mechanism.SCREENSHOT_TYPE:
                        // TODO: Implement screenshot mechanism
                        ScreenshotMechanism screenshotMechanism = (ScreenshotMechanism) mechanism;
                        if (screenshotMechanism.getImagePath() != null) {
                            if (screenshotFeedbacks == null) {
                                screenshotFeedbacks = new ArrayList<>();
                            }
                            screenshotFeedbacks.add(new ScreenshotFeedback(screenshotMechanism, screenshotFeedbacks.size()));
                        }
                        break;
                    case Mechanism.TEXT_TYPE:
                        if (textFeedbacks == null) {
                            textFeedbacks = new ArrayList<>();
                        }
                        textFeedbacks.add(new TextFeedback((TextMechanism) mechanism));
                        break;
                    default:
                        // should never happen!
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

    public List<CategoryFeedback> getCategoryFeedbacks() {
        return categoryFeedbacks;
    }

    public long getConfigurationId() {
        return configurationId;
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

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public void setAttachmentFeedbacks(List<AttachmentFeedback> attachmentFeedbacks) {
        this.attachmentFeedbacks = attachmentFeedbacks;
    }

    public void setAudioFeedbacks(List<AudioFeedback> audioFeedbacks) {
        this.audioFeedbacks = audioFeedbacks;
    }

    public void setCategoryFeedbacks(List<CategoryFeedback> categoryFeedbacks) {
        this.categoryFeedbacks = categoryFeedbacks;
    }

    public void setConfigurationId(long configurationId) {
        this.configurationId = configurationId;
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
