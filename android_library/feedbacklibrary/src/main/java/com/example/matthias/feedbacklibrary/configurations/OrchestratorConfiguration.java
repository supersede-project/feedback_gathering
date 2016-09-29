package com.example.matthias.feedbacklibrary.configurations;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class holds the full configuration which is retrieved from the feedback orchestrator.
 */
public class OrchestratorConfiguration implements Serializable {
    @SerializedName("general_configurations")
    private List<Map<String, Object>> generalConfigurationItems;
    private List<MechanismConfigurationItem> mechanisms;
    @SerializedName("pull_configurations")
    private List<PullConfigurationItem> pullConfigurationItems;

    public List<Map<String, Object>> getGeneralConfigurationItems() {
        return generalConfigurationItems;
    }

    public List<MechanismConfigurationItem> getMechanisms() {
        return mechanisms;
    }

    public List<PullConfigurationItem> getPullConfigurationItems() {
        return pullConfigurationItems;
    }

    public void setGeneralConfigurationItems(List<Map<String, Object>> generalConfigurationItems) {
        this.generalConfigurationItems = generalConfigurationItems;
    }

    public void setMechanisms(List<MechanismConfigurationItem> mechanisms) {
        this.mechanisms = mechanisms;
    }

    public void setPullConfigurationItems(List<PullConfigurationItem> pullConfigurationItems) {
        this.pullConfigurationItems = pullConfigurationItems;
    }
}
