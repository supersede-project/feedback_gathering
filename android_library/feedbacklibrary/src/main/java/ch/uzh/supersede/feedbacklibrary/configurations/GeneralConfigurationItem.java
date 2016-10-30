package ch.uzh.supersede.feedbacklibrary.configurations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * General configuration item.
 */
public class GeneralConfigurationItem implements Serializable {
    private String createdAt;
    private long id;
    private List<Map<String, Object>> parameters;

    /**
     * This method returns the date of creation as a string.
     *
     * @return the creation date as a string
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * This method returns the id of the general configuration item.
     *
     * @return the general configuration item id
     */
    public long getId() {
        return id;
    }

    /**
     * This method returns all parameters of the general configuration item.
     *
     * @return the parameters
     */
    public List<Map<String, Object>> getParameters() {
        return parameters;
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
     * This method sets the id of the general configuration item.
     *
     * @param id the general configuration item id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * This method sets the parameters of the general configuration item.
     *
     * @param parameters the parameters
     */
    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }
}
