package ch.uzh.supersede.feedbacklibrary.models;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import java.util.List;

/**
 * Mechanism model
 */
public abstract class Mechanism {
    public static final String ATTACHMENT_TYPE = "ATTACHMENT_TYPE";
    public static final String AUDIO_TYPE = "AUDIO_TYPE";
    public static final String CATEGORY_TYPE = "CATEGORY_TYPE";
    public static final String DIALOG_TYPE = "DIALOG_TYPE";
    public static final String IMAGE_TYPE = "IMAGE_TYPE";
    public static final String RATING_TYPE = "RATING_TYPE";
    public static final String SCREENSHOT_TYPE = "SCREENSHOT_TYPE";
    public static final String TEXT_TYPE = "TEXT_TYPE";

    private boolean canBeActivated;
    private long id;
    private boolean isActive;
    private int order;
    private String type;

    public Mechanism(String type, MechanismConfigurationItem item) {
        this.canBeActivated = item.canBeActivated();
        this.id = item.getId();
        this.isActive = item.isActive();
        this.order = item.getOrder();
        this.type = type;
    }

    /**
     * This method returns if the mechanism can be activated/deactivated by the user.
     *
     * @return true if the mechanism can be activated, false otherwise
     */
    public boolean canBeActivated() {
        return this.canBeActivated;
    }

    /**
     * This method returns the id of the mechanism.
     *
     * @return the mechanism id
     */
    public long getId() {
        return id;
    }

    /**
     * This method returns the order of the mechanism.
     *
     * @return the order
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * This method returns the type of the mechanism.
     *
     * @return the type, i.e., ATTACHMENT_TYPE, AUDIO_TYPE, CATEGORY_TYPE, RATING_TYPE, SCREENSHOT_TYPE or TEXT_TYPE
     */
    public String getType() {
        return this.type;
    }

    /**
     * This method returns if the mechanism is active.
     *
     * @return true if the mechanism is active, false otherwise
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * This method returns if the input of the mechanism is valid.
     *
     * @param errorMessage error message to show if the validation fails
     * @return true if the input of the mechanism is valid, false otherwise
     */
    public abstract boolean isValid(List<String> errorMessage);

    /**
     * This method sets if the mechanism can be activated/deactivated by the user.
     *
     * @param canBeActivated true if the mechanism can be activated, false otherwise
     */
    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }

    /**
     * This method sets if the mechanism is active.
     *
     * @param isActive true if the mechanism is active, false otherwise
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * This method sets the order of the mechanism.
     *
     * @param order the order
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
