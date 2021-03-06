
package ch.fhnw.cere.repository.models.orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.supersede.integration.api.feedback.orchestrator.types.ConfigurationMechanism;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Mechanism {
    @JsonProperty("configurationsId")
    protected Long configurationsId;
    @JsonProperty("createdAt")
    protected String createdAt;
    @JsonProperty("active")
    protected Boolean active;
    @JsonProperty("id")
    protected Long id;
    @JsonProperty("type")
    protected String type;
    @JsonProperty("parameters")
    protected List<Parameter> parameters = null;
    @JsonProperty("canBeActivated")
    protected Boolean canBeActivated;
    @JsonProperty("order")
    protected Long order;

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

    @Override
    public String toString() {
        return "Mechanism{" +
                "configurationsId=" + configurationsId +
                ", createdAt='" + createdAt + '\'' +
                ", active=" + active +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", parameters=" + parameters +
                ", canBeActivated=" + canBeActivated +
                ", order=" + order +
                '}';
    }

    public static List<Mechanism> filterByCategoryParameterId(List<Mechanism> mechanisms, long categoryParameterId) {
        List<Mechanism> foundMechanisms = new ArrayList<>();

        for(Mechanism mechanism : mechanisms) {
            Parameter optionParameter = null;
            for(Parameter parameter : mechanism.getParameters()) {
                if(parameter.getKey().equals("options")) {
                    optionParameter = parameter;
                }
            }
            if(optionParameter != null) {
                for(Parameter categoryParameter : optionParameter.getParameters()) {
                    if(categoryParameter.getId().equals(categoryParameterId)) {
                        foundMechanisms.add(mechanism);
                    }
                }
            }
        }

        return foundMechanisms;
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
