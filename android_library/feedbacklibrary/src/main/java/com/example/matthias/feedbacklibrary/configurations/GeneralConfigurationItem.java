package com.example.matthias.feedbacklibrary.configurations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * General configuration item.
 */
public class GeneralConfigurationItem implements Serializable {
    private String createdAt;
    private long id;
    private List<Map<String, Object>> parameters;

    public String getCreatedAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public List<Map<String, Object>> getParameters() {
        return parameters;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }
}
