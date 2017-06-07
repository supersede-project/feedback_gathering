
package ch.fhnw.cere.repository.models.orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Mechanism {
    @JsonProperty("configurationsId")
    public Long configurationsId;
    @JsonProperty("createdAt")
    public String createdAt;
    @JsonProperty("active")
    public Boolean active;
    @JsonProperty("id")
    public Long id;
    @JsonProperty("type")
    public String type;
    @JsonProperty("parameters")
    public List<Parameter> parameters = null;
    @JsonProperty("canBeActivated")
    public Boolean canBeActivated;
    @JsonProperty("order")
    public Long order;

    public Mechanism() {
    }

    public Mechanism(Long configurationsId, String createdAt, Boolean active, Long id, String type, List<Parameter> parameters, Boolean canBeActivated, Long order) {
        this.configurationsId = configurationsId;
        this.createdAt = createdAt;
        this.active = active;
        this.id = id;
        this.type = type;
        this.parameters = parameters;
        this.canBeActivated = canBeActivated;
        this.order = order;
    }

    public Long getConfigurationsId() {
        return configurationsId;
    }

    public void setConfigurationsId(Long configurationsId) {
        this.configurationsId = configurationsId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Boolean getCanBeActivated() {
        return canBeActivated;
    }

    public void setCanBeActivated(Boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
}
