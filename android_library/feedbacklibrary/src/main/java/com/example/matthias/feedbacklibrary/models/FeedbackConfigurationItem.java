package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthias on 28.03.2016.
 */
public class FeedbackConfigurationItem implements Serializable {
    private boolean canBeActivated;
    private boolean isActive;
    private int order;
    private String type;
    private List<Map<String, Object>> parameters;

    public boolean canBeActivated() {
        return canBeActivated;
    }

    public boolean isActive() {
        return isActive;
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

    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
