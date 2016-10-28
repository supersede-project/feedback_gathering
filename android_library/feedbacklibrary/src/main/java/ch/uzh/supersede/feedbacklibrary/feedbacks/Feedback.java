package ch.uzh.supersede.feedbacklibrary.feedbacks;

import android.os.Build;
import android.util.Log;

import ch.uzh.supersede.feedbacklibrary.models.AttachmentMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;
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

    /**
     * This method returns the application id.
     *
     * @return the application id
     */
    public long getApplicationId() {
        return applicationId;
    }

    /**
     * This method returns the attachment feedbacks.
     *
     * @return the attachment feedbacks
     */
    public List<AttachmentFeedback> getAttachmentFeedbacks() {
        return attachmentFeedbacks;
    }

    /**
     * This method returns the audio feedbacks.
     *
     * @return the audio feedbacks
     */
    public List<AudioFeedback> getAudioFeedbacks() {
        return audioFeedbacks;
    }

    /**
     * This method returns the category feedbacks.
     *
     * @return the category feedbacks
     */
    public List<HashMap<String, Object>> getCategoryFeedbacks() {
        return categoryFeedbacks;
    }

    /**
     * This method returns the configuration id of the active configuration from which the feedback is sent.
     *
     * @return the configuration id
     */
    public long getConfigurationId() {
        return configurationId;
    }

    /**
     * This method returns the context information.
     *
     * @return the context information
     */
    public Map<String, Object> getContextInformation() {
        return contextInformation;
    }

    /**
     * This method returns the language.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * This method returns the rating feedbacks.
     *
     * @return the rating feedbacks.
     */
    public List<RatingFeedback> getRatingFeedbacks() {
        return ratingFeedbacks;
    }

    /**
     * This method returns the screenshot feedbacks.
     *
     * @return the screenshot feedbacks
     */
    public List<ScreenshotFeedback> getScreenshotFeedbacks() {
        return screenshotFeedbacks;
    }

    /**
     * This method returns the text feedbacks.
     *
     * @return the text feedbacks
     */
    public List<TextFeedback> getTextFeedbacks() {
        return textFeedbacks;
    }

    /**
     * This method returns the title of the feedback.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method returns the user identification.
     *
     * @return the user identification
     */
    public String getUserIdentification() {
        return userIdentification;
    }

    /**
     * This method initializes the context information
     */
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

    /**
     * This method sets the application id.
     *
     * @param applicationId the application id
     */
    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * This method sets the attachment feedbacks.
     *
     * @param attachmentFeedbacks the attachment feedbacks
     */
    public void setAttachmentFeedbacks(List<AttachmentFeedback> attachmentFeedbacks) {
        this.attachmentFeedbacks = attachmentFeedbacks;
    }

    /**
     * This method sets the audio feedbacks.
     *
     * @param audioFeedbacks the audio feedbacks
     */
    public void setAudioFeedbacks(List<AudioFeedback> audioFeedbacks) {
        this.audioFeedbacks = audioFeedbacks;
    }

    /**
     * This method sets the category feedbacks.
     *
     * @param categoryFeedbacks the category feedbacks
     */
    public void setCategoryFeedbacks(List<HashMap<String, Object>> categoryFeedbacks) {
        this.categoryFeedbacks = categoryFeedbacks;
    }

    /**
     * This method sets the configuration id of the active configuration from which the feedback is sent.
     *
     * @param configurationId the configuration id
     */
    public void setConfigurationId(long configurationId) {
        this.configurationId = configurationId;
    }

    public void setContextInformation(Map<String, Object> contextInformation) {
        this.contextInformation = contextInformation;
    }

    /**
     * This method sets the language.
     *
     * @param language the language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * This method sets the rating feedbacks.
     *
     * @param ratingFeedbacks the rating feedbacks
     */
    public void setRatingFeedbacks(List<RatingFeedback> ratingFeedbacks) {
        this.ratingFeedbacks = ratingFeedbacks;
    }

    /**
     * This method sets the screenshot feedbacks.
     *
     * @param screenshotFeedbacks the screenshot feedbacks
     */
    public void setScreenshotFeedbacks(List<ScreenshotFeedback> screenshotFeedbacks) {
        this.screenshotFeedbacks = screenshotFeedbacks;
    }

    /**
     * This method sets the text feedbacks.
     *
     * @param textFeedbacks the text feedbacks
     */
    public void setTextFeedbacks(List<TextFeedback> textFeedbacks) {
        this.textFeedbacks = textFeedbacks;
    }

    /**
     * This method sets the title of the feedback.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method sets the user identification.
     *
     * @param userIdentification the user identification
     */
    public void setUserIdentification(String userIdentification) {
        this.userIdentification = userIdentification;
    }
}
