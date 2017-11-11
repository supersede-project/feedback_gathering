package ch.fhnw.cere.repository.models.orchestrator;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    @JsonProperty("createdAt")
    protected String createdAt;
    @JsonProperty("configurations")
    protected List<Configuration> configurations = null;
    @JsonProperty("name")
    protected String name;
    @JsonProperty("id")
    protected Long id;
    @JsonProperty("state")
    protected Long state;
    @JsonProperty("generalConfiguration")
    protected GeneralConfiguration generalConfiguration;

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
        Optional<Configuration> configurationOptional = this.configurations.stream().filter(conf -> conf.getId() == configurationId).findFirst();
        if(configurationOptional.isPresent()) {
            Configuration configuration = configurationOptional.get();
            Optional<Mechanism> mechanismOptional = configuration.getMechanisms().stream().filter(
                    mechanism -> mechanism.getId() == mechanismId
            ).findFirst();
            return mechanismOptional.orElse(null);
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
