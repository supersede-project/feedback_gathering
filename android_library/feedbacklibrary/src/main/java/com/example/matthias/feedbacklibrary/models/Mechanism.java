package com.example.matthias.feedbacklibrary.models;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;

import java.io.Serializable;
import java.util.List;

/**
 * Mechanism model
 */
public abstract class Mechanism implements Serializable {
    public static final String AUDIO_TYPE = "AUDIO_TYPE";
    public static final String CHOICE_TYPE = "CHOICE_TYPE";
    public static final String RATING_TYPE = "RATING_TYPE";
    public static final String SCREENSHOT_TYPE = "SCREENSHOT_TYPE";
    public static final String TEXT_TYPE = "TEXT_TYPE";

    private boolean isActive;
    private boolean canBeActivated;
    private String type;
    private int order;

    public Mechanism(String type, MechanismConfigurationItem item) {
        this.isActive = item.isActive();
        this.canBeActivated = item.canBeActivated();
        this.type = type;
        this.order = item.getOrder();
    }

    /**
     * This method returns true if the mechanism can be activated/deactivated by the user, false otherwise.
     *
     * @return canBeActivated
     */
    public boolean canBeActivated() {
        return this.canBeActivated;
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
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * This method returns true if the mechanism is active, false otherwise.
     *
     * @return isActive
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * This method returns true if the input of the mechanism is valid, false otherwise.
     *
     * @param errorMessage error message to show if the validation fails
     * @return the validity value
     */
    public abstract boolean isValid(List<String> errorMessage);

    /**
     * @param canBeActivated the value to set
     */
    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }

    /**
     * @param isActive the value to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @param order the value to set
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
