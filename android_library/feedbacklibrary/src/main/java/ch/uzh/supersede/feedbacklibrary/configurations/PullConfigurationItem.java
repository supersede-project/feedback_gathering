package ch.uzh.supersede.feedbacklibrary.configurations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PullConfigurationItem implements Serializable {
    private boolean active;
    private List<MechanismConfigurationItem> mechanisms;
    private List<Map<String, Object>> parameters;

    public List<MechanismConfigurationItem> getMechanisms() {
        return mechanisms;
    }

    public List<Map<String, Object>> getParameters() {
        return parameters;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMechanisms(List<MechanismConfigurationItem> mechanisms) {
        this.mechanisms = mechanisms;
    }

    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }
}
