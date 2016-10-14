package com.example.matthias.feedbacklibrary.configurations;

import com.example.matthias.feedbacklibrary.models.AttachmentMechanism;
import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.example.matthias.feedbacklibrary.models.CategoryMechanism;
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
    private boolean active;
    private boolean canBeActivated;
    private long id;
    private int order;
    private List<Map<String, Object>> parameters;
    private String type;

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
                case Mechanism.ATTACHMENT_TYPE:
                    return new AttachmentMechanism(this);
                case Mechanism.AUDIO_TYPE:
                    return new AudioMechanism(this);
                case Mechanism.CATEGORY_TYPE:
                    return new CategoryMechanism(this);
                case Mechanism.RATING_TYPE:
                    return new RatingMechanism(this);
                case Mechanism.SCREENSHOT_TYPE:
                    return new ScreenshotMechanism(this);
                case Mechanism.TEXT_TYPE:
                    return new TextMechanism(this);
                default:
                    break;
            }
        }
        return null;
    }

    public long getId() {
        return id;
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

    public boolean isCanBeActivated() {
        return canBeActivated;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }

    public void setId(long id) {
        this.id = id;
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
