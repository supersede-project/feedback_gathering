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

    /**
     * This method returns the date of creation as a string.
     *
     * @return the creation date as a string
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * This method returns the id of the general configuration.
     *
     * @return the general configuration id
     */
    public long getId() {
        return id;
    }

    /**
     * This method returns all parameters of the general configuration.
     *
     * @return the parameters
     */
    public List<Map<String, Object>> getParameters() {
        return parameters;
    }
}