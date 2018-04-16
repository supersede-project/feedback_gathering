package ch.uzh.supersede.feedbacklibrary.configurations;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ConfigurationItem implements Serializable {
    private String dateOfCreation;
    @SerializedName("generalConfiguration")
    private GeneralConfigurationItem generalConfigurationItem;
    private long id;
    private String type;
    @SerializedName("mechanisms")
    private List<MechanismConfigurationItem> mechanismConfigurationItems;

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public GeneralConfigurationItem getGeneralConfigurationItem() {
        return generalConfigurationItem;
    }

    public void setGeneralConfigurationItem(GeneralConfigurationItem generalConfigurationItem) {
        this.generalConfigurationItem = generalConfigurationItem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MechanismConfigurationItem> getMechanismConfigurationItems() {
        return mechanismConfigurationItems;
    }

    public void setMechanismConfigurationItems(List<MechanismConfigurationItem> mechanismConfigurationItems) {
        this.mechanismConfigurationItems = mechanismConfigurationItems;
    }
}
