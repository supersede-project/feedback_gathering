package ch.uzh.supersede.feedbacklibrary.configurations;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;

public class ConfigurationItem implements Serializable {
    private String dateOfCreation;
    @SerializedName("generalConfiguration")
    private GeneralConfigurationItem generalConfigurationItem;
    private long id;
    private String type;
    @SerializedName("mechanisms")
    private List<AbstractFeedbackPart> abstractFeedbackParts;

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

    public List<AbstractFeedbackPart> getAbstractFeedbackParts() {
        return abstractFeedbackParts;
    }

    public void setAbstractFeedbackParts(List<AbstractFeedbackPart> abstractFeedbackParts) {
        this.abstractFeedbackParts = abstractFeedbackParts;
    }
}
