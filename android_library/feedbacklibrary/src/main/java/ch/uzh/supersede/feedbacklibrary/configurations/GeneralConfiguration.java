package ch.uzh.supersede.feedbacklibrary.configurations;

import java.util.List;
import java.util.Map;

public class GeneralConfiguration {
    private String dateOfCreation;
    private long id;
    private List<Map<String, Object>> parameters;

    public GeneralConfiguration(GeneralConfigurationItem generalConfigurationItem) {
        if (generalConfigurationItem != null) {
            dateOfCreation = generalConfigurationItem.getDateOfCreation();
            id = generalConfigurationItem.getId();
            parameters = generalConfigurationItem.getParameters();
        }
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Map<String, Object>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }
}