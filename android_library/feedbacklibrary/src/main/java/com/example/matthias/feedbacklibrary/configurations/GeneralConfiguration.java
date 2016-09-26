package com.example.matthias.feedbacklibrary.configurations;

import java.util.List;
import java.util.Map;

/**
 * General configuration.
 */
public class GeneralConfiguration {
    private String createdAt;
    private long id;
    private List<Map<String, Object>> parameters;

    public GeneralConfiguration(GeneralConfigurationItem generalConfigurationItem) {
        createdAt = generalConfigurationItem.getCreatedAt();
        id = generalConfigurationItem.getId();
        parameters = generalConfigurationItem.getParameters();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public List<Map<String, Object>> getParameters() {
        return parameters;
    }
}