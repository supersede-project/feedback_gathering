package ch.uzh.supersede.feedbacklibrary.configurations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GeneralConfigurationItem implements Serializable {
    private String dateOfCreation;
    private long id;
    private List<Map<String, Object>> parameters;

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
