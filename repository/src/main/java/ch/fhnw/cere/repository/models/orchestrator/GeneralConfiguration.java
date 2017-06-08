
package ch.fhnw.cere.repository.models.orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralConfiguration {
    @JsonProperty("createdAt")
    protected String createdAt;
    @JsonProperty("name")
    protected String name;
    @JsonProperty("id")
    protected Long id;
    @JsonProperty("parameters")
    protected List<Parameter> parameters = null;

    public GeneralConfiguration() {
    }

    public GeneralConfiguration(String createdAt, String name, Long id, List<Parameter> parameters) {
        this.createdAt = createdAt;
        this.name = name;
        this.id = id;
        this.parameters = parameters;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
