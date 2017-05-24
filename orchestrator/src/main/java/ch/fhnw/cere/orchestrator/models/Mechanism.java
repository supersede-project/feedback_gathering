package ch.fhnw.cere.orchestrator.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Mechanism {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private MechanismType type;
    private Date createdAt;
    private Date updatedAt;
    @JsonIgnore
    @OneToMany(mappedBy = "mechanism")
    private List<ConfigurationMechanism> configurationMechanisms;
    @OneToMany(mappedBy = "mechanism")
    private List<Parameter> parameters;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public Mechanism() {
    }

    public Mechanism(MechanismType type, Date createdAt, Date updatedAt, List<ConfigurationMechanism> configurationMechanisms, List<Parameter> parameters) {
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.configurationMechanisms = configurationMechanisms;
        this.parameters = parameters;
    }

    public Mechanism(MechanismType type, List<ConfigurationMechanism> configurationMechanisms, List<Parameter> parameters) {
        this.type = type;
        this.configurationMechanisms = configurationMechanisms;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return String.format(
                "Mechanism[id=%d, type='%s', parameters='%s']",
                id, type, this.getParameters().stream().map(Object::toString)
                        .collect(Collectors.joining(", ")));
    }

    List<Parameter> parametersByLanguage(String language, String fallbackLanguage) {
        if(this.getParameters() == null) {
            return null;
        }
        Map<String, Parameter> keyValuePairs = new HashMap<>();
        for(Parameter parameter : this.getParameters()) {
            if(parameter.getParameters() != null && parameter.getParameters().size() > 0) {
                parameter.setParameters(parameter.parametersByLanguage(language, fallbackLanguage));
            }

            if(keyValuePairs.containsKey(parameter.getKey())) {
                if(parameter.getLanguage().equals(language)) {
                    keyValuePairs.put(parameter.getKey(), parameter);
                } else if (!keyValuePairs.get(parameter.getKey()).getLanguage().equals(language) && parameter.getLanguage().equals(fallbackLanguage)) {
                    keyValuePairs.put(parameter.getKey(), parameter);
                }
            } else if(parameter.getLanguage().equals(language) || parameter.getLanguage().equals(fallbackLanguage)) {
                keyValuePairs.put(parameter.getKey(), parameter);
            }
        }
        return new ArrayList<Parameter>(keyValuePairs.values());
    }

    void filterByLanguage(String language, String fallbackLanguage) {
        if(this.getParameters() == null) {
            return;
        }
        for(Parameter parameter : this.getParameters()) {
            parameter.filterByLanguage(language, fallbackLanguage);
        }
        this.setParameters(this.parametersByLanguage(language, fallbackLanguage));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MechanismType getType() {
        return type;
    }

    public void setType(MechanismType type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ConfigurationMechanism> getConfigurationMechanisms() {
        return configurationMechanisms;
    }

    public void setConfigurationMechanisms(List<ConfigurationMechanism> configurationMechanisms) {
        this.configurationMechanisms = configurationMechanisms;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}


