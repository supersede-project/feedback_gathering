package ch.uzh.supersede.feedbacklibrary.configurations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Pull configuration item.
 */
public class PullConfigurationItem implements Serializable {
    private boolean active;
    private List<MechanismConfigurationItem> mechanisms;
    private List<Map<String, Object>> parameters;

    /**
     * This method returns all mechanisms of the pull configuration item.
     *
     * @return all mechanisms
     */
    public List<MechanismConfigurationItem> getMechanisms() {
        return mechanisms;
    }

    /**
     * This method returns all the parameters of the pull configuration item.
     *
     * @return the parameters
     */
    public List<Map<String, Object>> getParameters() {
        return parameters;
    }

    /**
     * This method returns if the pull configuration item is active.
     *
     * @return true if it is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * This method sets if the pull configuration item is active.
     *
     * @param active true if it is active, false otherwise
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * This method sets all mechanisms of the pull configuration item.
     *
     * @param mechanisms all mechanisms
     */
    public void setMechanisms(List<MechanismConfigurationItem> mechanisms) {
        this.mechanisms = mechanisms;
    }

    /**
     * This methods sets all the parameters of the pull configuration item.
     *
     * @param parameters the parameters
     */
    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }
}
