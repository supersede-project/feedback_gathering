package ch.uzh.supersede.feedbacklibrary.configurations;

import java.io.Serializable;
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

public class MechanismConfigurationItem implements Serializable {
    private boolean isActive;
    private boolean isActivatePossible;
    private long id;
    private int order;
    private List<Map<String, Object>> parameters;
    private String type;

    public AbstractMechanism createMechanism() {
        if (type != null) {
            switch (type) {
                case ATTACHMENT_TYPE:
                    return new AttachmentMechanism(this);
                case AUDIO_TYPE:
                    return new AudioMechanism(this);
                case CATEGORY_TYPE:
                    return new CategoryMechanism(this);
                case RATING_TYPE:
                    return new RatingMechanism(this);
                case SCREENSHOT_TYPE:
                    return new ScreenshotMechanism(this);
                case TEXT_TYPE:
                    return new TextMechanism(this);
                default:
                    break;
            }
        }
        return null;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActivatePossible() {
        return isActivatePossible;
    }

    public void setActivatePossible(boolean isActivatePossible) {
        this.isActivatePossible = isActivatePossible;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<Map<String, Object>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
