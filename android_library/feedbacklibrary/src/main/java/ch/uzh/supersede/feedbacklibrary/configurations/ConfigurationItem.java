package ch.uzh.supersede.feedbacklibrary.configurations;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;

public class ConfigurationItem implements Serializable {
    private String dateOfCreation;
    @SerializedName("generalConfiguration")
    private GeneralConfigurationItem generalConfigurationItem;
    private long id;
    private String type;
    @SerializedName("mechanisms")
    private List<AbstractMechanism> abstractMechanisms;

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

    public List<AbstractMechanism> getAbstractMechanisms() {
        return abstractMechanisms;
    }

    public void setAbstractMechanisms(List<AbstractMechanism> abstractMechanisms) {
        this.abstractMechanisms = abstractMechanisms;
    }
}
