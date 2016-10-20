package com.example.matthias.feedbacklibrary.configurations;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class holds the full configuration which is retrieved from the feedback orchestrator.
 */
public class OrchestratorConfigurationItem implements Serializable {
    @SerializedName("configurations")
    private List<ConfigurationItem> configurationItems;
    private String createdAt;
    private long id;
    private String name;
    @SerializedName("generalConfiguration")
    private GeneralConfigurationItem generalConfigurationItem;
    private long state;

    /**
     * This method returns all the configuration items of the orchestrator configuration item.
     *
     * @return the configuration items
     */
    public List<ConfigurationItem> getConfigurationItems() {
        return configurationItems;
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
     * This method returns the general configuration item of the orchestrator configuration item.
     *
     * @return the general configuration item
     */
    public GeneralConfigurationItem getGeneralConfigurationItem() {
        return generalConfigurationItem;
    }

    /**
     * This method returns the id of the orchestrator configuration item.
     *
     * @return the orchestrator configuration item id
     */
    public long getId() {
        return id;
    }

    /**
     * This method returns the name of the orchestrator configuration item.
     *
     * @return the orchestrator configuration item name
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the state of the orchestrator configuration item.
     *
     * @return the orchestrator configuration item state
     */
    public long getState() {
        return state;
    }

    /**
     * This method sets all the configuration items of the orchestrator configuration item.
     *
     * @param configurationItems the configuration items
     */
    public void setConfigurationItems(List<ConfigurationItem> configurationItems) {
        this.configurationItems = configurationItems;
    }

    /**
     * This method sets the date of creation.
     *
     * @param createdAt the creation date as a string
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method sets the general configuration item of the orchestrator configuration item.
     *
     * @param generalConfigurationItem the general configuration item
     */
    public void setGeneralConfigurationItem(GeneralConfigurationItem generalConfigurationItem) {
        this.generalConfigurationItem = generalConfigurationItem;
    }

    /**
     * This method sets the id of the orchestrator configuration item.
     *
     * @param id the orchestrator configuration item id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * This method sets the name of the orchestrator configuration item.
     *
     * @param name the orchestrator configuration item name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the state of the orchestrator configuration item.
     *
     * @param state the orchestrator configuration item state
     */
    public void setState(long state) {
        this.state = state;
    }
}
