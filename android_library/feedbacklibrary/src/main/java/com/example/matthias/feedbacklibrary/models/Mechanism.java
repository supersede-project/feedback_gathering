package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public abstract class Mechanism implements Serializable {
    private boolean canBeActivated;
    private boolean isActive;
    private int order;
    private String type;

    public Mechanism(String type, boolean canBeActivated, boolean isActive, int order) {
        this.type = type;
        this.canBeActivated = canBeActivated;
        this.isActive = isActive;
        this.order = order;
    }

    /**
     * Returns true if the mechanism can be activated/deactivated by the user, false otherwise
     *
     * @return canBeActivated
     */
    public boolean canBeActivated() {
        return this.canBeActivated;
    }

    /**
     * Returns the order of the mechanism
     *
     * @return the order
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * Returns the type of the mechanism
     *
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns true if the mechanism is active, false otherwise.
     *
     * @return isActive
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     *
     * @param canBeActivated the value to set
     */
    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }

    /**
     *
     * @param isActive the value to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @param order the value to set
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
