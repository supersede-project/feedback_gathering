package ch.uzh.supersede.feedbacklibrary.configurations;

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

    /**
     * This method returns the date of creation as a string.
     *
     * @return the creation date as a string
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * This method returns the general configuration item.
     *
     * @return the general configuration item
     */
    public GeneralConfigurationItem getGeneralConfigurationItem() {
        return generalConfigurationItem;
    }

    /**
     * This method returns the id of the configuration item.
     *
     * @return the configuration item id
     */
    public long getId() {
        return id;
    }

    /**
     * This method returns all mechanism configuration items of the configuration item.
     *
     * @return all mechanisms
     */
    public List<MechanismConfigurationItem> getMechanismConfigurationItems() {
        return mechanismConfigurationItems;
    }

    /**
     * This method returns the type of the configuration item.
     *
     * @return the configuration item type, i.e., ATTACHMENT_TYPE, AUDIO_TYPE, CATEGORY_TYPE, RATING_TYPE, SCREENSHOT_TYPE or TEXT_TYPE
     */
    public String getType() {
        return type;
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
     * This method sets the general configuration item.
     *
     * @param generalConfigurationItem the general configuration item
     */
    public void setGeneralConfigurationItem(GeneralConfigurationItem generalConfigurationItem) {
        this.generalConfigurationItem = generalConfigurationItem;
    }

    /**
     * This method sets the id of the configuration item.
     *
     * @param id the configuration item id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * This method sets all mechanism configuration items of the configuration item.
     *
     * @param mechanismConfigurationItems all mechanisms
     */
    public void setMechanismConfigurationItems(List<MechanismConfigurationItem> mechanismConfigurationItems) {
        this.mechanismConfigurationItems = mechanismConfigurationItems;
    }

    /**
     * This method sets the type of the configuration item.
     *
     * @param type the configuration item type, i.e., ATTACHMENT_TYPE, AUDIO_TYPE, CATEGORY_TYPE, RATING_TYPE, SCREENSHOT_TYPE or TEXT_TYPE
     */
    public void setType(String type) {
        this.type = type;
    }
}
