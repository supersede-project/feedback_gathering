package com.example.matthias.feedbacklibrary.configurations;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Configuration item.
 */
public class ConfigurationItem implements Serializable {
    private String createdAt;
    @SerializedName("generalConfiguration")
    private GeneralConfigurationItem generalConfigurationItem;
    private long id;
    private String type;
    @SerializedName("mechanisms")
    private List<MechanismConfigurationItem> mechanismConfigurationItems;

    public String getCreatedAt() {
        return createdAt;
    }

    public GeneralConfigurationItem getGeneralConfigurationItem() {
        return generalConfigurationItem;
    }

    public long getId() {
        return id;
    }

    public List<MechanismConfigurationItem> getMechanismConfigurationItems() {
        return mechanismConfigurationItems;
    }

    public String getType() {
        return type;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setGeneralConfigurationItem(GeneralConfigurationItem generalConfigurationItem) {
        this.generalConfigurationItem = generalConfigurationItem;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMechanismConfigurationItems(List<MechanismConfigurationItem> mechanismConfigurationItems) {
        this.mechanismConfigurationItems = mechanismConfigurationItems;
    }

    public void setType(String type) {
        this.type = type;
    }
}
