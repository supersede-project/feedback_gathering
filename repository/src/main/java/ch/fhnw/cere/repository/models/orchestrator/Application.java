package ch.fhnw.cere.repository.models.orchestrator;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    @JsonProperty("createdAt")
    public String createdAt;
    @JsonProperty("configurations")
    public List<Configuration> configurations = null;
    @JsonProperty("name")
    public String name;
    @JsonProperty("id")
    public Long id;
    @JsonProperty("state")
    public Long state;
    @JsonProperty("generalConfiguration")
    public GeneralConfiguration generalConfiguration;

    public Application() {
    }

    public Application(String createdAt, List<Configuration> configurations, String name, Long id, Long state, GeneralConfiguration generalConfiguration) {
        this.createdAt = createdAt;
        this.configurations = configurations;
        this.name = name;
        this.id = id;
        this.state = state;
        this.generalConfiguration = generalConfiguration;
    }

    public Mechanism mechanismByConfigurationIdAndMechanismId(long configurationId, long mechanismId) {
        Configuration configuration = this.configurations.stream().filter(conf -> conf.getId() == configurationId).findFirst().get();
        if(configuration != null) {
            return configuration.getMechanisms().stream().filter(
                    mechanism -> mechanism.getId() == mechanismId
            ).findFirst().get();
        } else {
            return null;
        }
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
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

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    public void setGeneralConfiguration(GeneralConfiguration generalConfiguration) {
        this.generalConfiguration = generalConfiguration;
    }
}
