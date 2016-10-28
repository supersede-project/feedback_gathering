package ch.uzh.supersede.feedbacklibrary.configurations;

import ch.uzh.supersede.feedbacklibrary.models.AttachmentMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;

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

    /**
     * This method returns if the mechanism configuration item can be activated.
     *
     * @return true if it can be activated, false otherwise
     */
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

    /**
     * This method returns the id of the mechanism configuration item.
     *
     * @return the mechanism configuration id
     */
    public long getId() {
        return id;
    }

    /**
     * This method returns the order of the mechanism configuration item.
     *
     * @return the mechanism configuration item order
     */
    public int getOrder() {
        return order;
    }

    /**
     * This method returns all parameters of the mechanism configuration item.
     *
     * @return the parameters
     */
    public List<Map<String, Object>> getParameters() {
        return parameters;
    }

    /**
     * This method returns the type of the mechanism configuration item.
     *
     * @return the mechanism configuration item type, i.e., ATTACHMENT_TYPE, AUDIO_TYPE, CATEGORY_TYPE, RATING_TYPE, SCREENSHOT_TYPE or TEXT_TYPE
     */
    public String getType() {
        return type;
    }

    /**
     * This method returns if the mechanism configuration item is active.
     *
     * @return true if it is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * This method returns if the mechanism configuration item can be activated.
     *
     * @return true if it can be activated, false otherwise
     */
    public boolean isCanBeActivated() {
        return canBeActivated;
    }

    /**
     * This method sets if the mechanism configuration item is active.
     *
     * @param active true if it is active, false otherwise
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * This method sets if the mechanism configuration item can be activated
     *
     * @param canBeActivated true if it can be activated, false otherwise
     */
    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }

    /**
     * This method sets the id of the mechanism configuration item.
     *
     * @param id the mechanism configuration item id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * This method sets the order of the mechanism configuration item.
     *
     * @param order the mechanism configuration item order
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * This method sets the parameters of the mechanism configuration item.
     *
     * @param parameters the parameters
     */
    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }

    /**
     * This method sets the type of the mechanism configuration item.
     *
     * @param type the mechanism configuration item type, i.e., ATTACHMENT_TYPE, AUDIO_TYPE, CATEGORY_TYPE, RATING_TYPE, SCREENSHOT_TYPE or TEXT_TYPE
     */
    public void setType(String type) {
        this.type = type;
    }
}
