package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

public abstract class AbstractMechanism implements Serializable {
    private boolean isActivatePossible;
    @Expose
    private long mechanismId;
    private boolean isActive;
    private int order;
    private String type;
    private String title;

    public AbstractMechanism() {
    }

    public AbstractMechanism(String type, MechanismConfigurationItem item) {
        this.isActivatePossible = item.isActivatePossible();
        this.mechanismId = item.getId();
        this.isActive = item.isActive();
        this.order = item.getOrder();
        this.type = type;
        initMechanism(item);
    }

    public void initMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            if (param.get("key") == null) {
                return;
            } else {
                handleMechanismParameter((String) param.get("key"), param.get("value"));
            }
        }
    }

    public void handleMechanismParameter(String key, Object value) {
        if (key.equals("title")) {
            setTitle((String) value);
        }
    }

    public abstract boolean isValid(List<String> errorMessage);

    public boolean isActivatePossible() {
        return isActivatePossible;
    }

    public void setActivatePossible(boolean isActivatePossible) {
        this.isActivatePossible = isActivatePossible;
    }

    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
