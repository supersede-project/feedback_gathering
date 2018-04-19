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

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.MechanismConstants.*;

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
    private List<AttachmentFeedback> attachmentFeedbackList = new ArrayList<>();
    @Expose
    private List<AudioFeedback> audioFeedbackList = new ArrayList<>();
    @Expose
    private List<HashMap<String, Object>> categoryFeedbackList = new ArrayList<>();
    @Expose
    private List<RatingFeedback> ratingFeedbackList = new ArrayList<>();
    @Expose
    private List<ScreenshotFeedback> screenshotFeedbackList = new ArrayList<>();
    @Expose
    private List<TextFeedback> textFeedbackList = new ArrayList<>();

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
                            audioFeedbackList.add(new AudioFeedback(audioMechanism, audioFeedbackList.size()));
                        }
                        break;
                    case CATEGORY_TYPE:
                        CategoryFeedback categoryFeedback = new CategoryFeedback((CategoryMechanism) mechanism);
                        categoryFeedbackList.addAll(categoryFeedback.getCategories());
                        break;
                    case RATING_TYPE:
                        ratingFeedbackList.add(new RatingFeedback((RatingMechanism) mechanism));
                        break;
                    case SCREENSHOT_TYPE:
                        ScreenshotMechanism screenshotMechanism = (ScreenshotMechanism) mechanism;
                        if (screenshotMechanism.getImagePath() != null) {
                            screenshotFeedbackList.add(new ScreenshotFeedback(screenshotMechanism, screenshotFeedbackList.size()));
                        }
                        break;
                    case TEXT_TYPE:
                        textFeedbackList.add(new TextFeedback((TextMechanism) mechanism));
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

    public List<AttachmentFeedback> getAttachmentFeedbacks() {
        return attachmentFeedbacks;
    }

    public void setAttachmentFeedbacks(List<AttachmentFeedback> attachmentFeedbacks) {
        this.attachmentFeedbacks = attachmentFeedbacks;
    }

    public List<AudioFeedback> getAudioFeedbacks() {
        return audioFeedbacks;
    }

    public void setAudioFeedbacks(List<AudioFeedback> audioFeedbacks) {
        this.audioFeedbacks = audioFeedbacks;
    }

    public List<HashMap<String, Object>> getCategoryFeedbacks() {
        return categoryFeedbacks;
    }

    public void setCategoryFeedbacks(List<HashMap<String, Object>> categoryFeedbacks) {
        this.categoryFeedbacks = categoryFeedbacks;
    }

    public List<RatingFeedback> getRatingFeedbacks() {
        return ratingFeedbacks;
    }

    public void setRatingFeedbacks(List<RatingFeedback> ratingFeedbacks) {
        this.ratingFeedbacks = ratingFeedbacks;
    }

    public List<ScreenshotFeedback> getScreenshotFeedbacks() {
        return screenshotFeedbacks;
    }

    public void setScreenshotFeedbacks(List<ScreenshotFeedback> screenshotFeedbacks) {
        this.screenshotFeedbacks = screenshotFeedbacks;
    }

    public List<TextFeedback> getTextFeedbacks() {
        return textFeedbacks;
    }

    public void setTextFeedbacks(List<TextFeedback> textFeedbacks) {
        this.textFeedbacks = textFeedbacks;
    }
}
