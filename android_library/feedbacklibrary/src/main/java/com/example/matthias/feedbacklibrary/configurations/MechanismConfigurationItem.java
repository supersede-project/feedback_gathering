package com.example.matthias.feedbacklibrary.configurations;

import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.example.matthias.feedbacklibrary.models.ChoiceMechanism;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.RatingMechanism;
import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Mechanism configuration item.
 */
public class MechanismConfigurationItem implements Serializable {
    private boolean canBeActivated;
    private boolean active;
    private int order;
    private String type;
    private List<Map<String, Object>> parameters;

    public boolean canBeActivated() {
        return canBeActivated;
    }

    /**
     * This method creates a mechanism based on its type.
     *
     * @return the mechanism
     */
    public Mechanism createMechanism() {
        if (type != null) {
            switch (type) {
                case Mechanism.AUDIO_TYPE:
                    // TODO: Implement
                    return new AudioMechanism(this);
                case Mechanism.CHOICE_TYPE:
                    return new ChoiceMechanism(this);
                case Mechanism.RATING_TYPE:
                    return new RatingMechanism(this);
                case Mechanism.SCREENSHOT_TYPE:
                    return new ScreenshotMechanism(this);
                case Mechanism.TEXT_TYPE:
                    return new TextMechanism(this);
                default:
                    // Should never happen!
                    break;
            }
        }

        return null; // Should never happen!
    }

    public int getOrder() {
        return order;
    }

    public List<Map<String, Object>> getParameters() {
        return parameters;
    }

    public String getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }

    public void setIsActive(boolean active) {
        this.active = active;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }

    public void setType(String type) {
        this.type = type;
    }
}
