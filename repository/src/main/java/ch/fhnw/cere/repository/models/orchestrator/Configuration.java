
package ch.fhnw.cere.repository.models.orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration {
    @JsonProperty("createdAt")
    protected String createdAt;
    @JsonProperty("id")
    protected Long id;
    @JsonProperty("generalConfiguration")
    protected GeneralConfiguration generalConfiguration;
    @JsonProperty("type")
    protected String type;
    @JsonProperty("mechanisms")
    protected List<Mechanism> mechanisms = null;

    public Configuration() {
    }

    public Configuration(String createdAt, Long id, GeneralConfiguration generalConfiguration, String type, List<Mechanism> mechanisms) {
        this.createdAt = createdAt;
        this.id = id;
        this.generalConfiguration = generalConfiguration;
        this.type = type;
        this.mechanisms = mechanisms;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "createdAt='" + createdAt + '\'' +
                ", id=" + id +
                ", generalConfiguration=" + generalConfiguration +
                ", type='" + type + '\'' +
                ", mechanisms=" + mechanisms +
                '}';
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    public void setGeneralConfiguration(GeneralConfiguration generalConfiguration) {
        this.generalConfiguration = generalConfiguration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Mechanism> getMechanisms() {
        return mechanisms;
    }

    public void setMechanisms(List<Mechanism> mechanisms) {
        this.mechanisms = mechanisms;
    }
}
