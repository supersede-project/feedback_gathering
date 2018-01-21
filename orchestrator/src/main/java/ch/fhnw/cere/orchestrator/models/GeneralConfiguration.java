package ch.fhnw.cere.orchestrator.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class GeneralConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "generalConfiguration", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Parameter> parameters;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @JsonIgnore
    @OneToOne(mappedBy="generalConfiguration", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Application application;

    @JsonIgnore
    @OneToOne(mappedBy="generalConfiguration", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Configuration configuration;

    public GeneralConfiguration() {
    }

    public GeneralConfiguration(String name, Date createdAt, Date updatedAt, List<Parameter> parameters, Application application, Configuration configuration) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parameters = parameters;
        this.application = application;
        this.configuration = configuration;
    }

    public void filterByLanguage(String language, String fallbackLanguage) {
        if(this.getParameters() == null) {
            return;
        }
        for(Parameter parameter : this.getParameters()) {
            parameter.filterByLanguage(language, fallbackLanguage);
        }
        this.setParameters(this.parametersByLanguage(language, fallbackLanguage));
    }

    @Override
    public String toString() {
        return String.format(
                "GeneralConfiguration[id=%d, name='%s']",
                id, name);
    }

    public List<Parameter> parametersByLanguage(String language, String fallbackLanguage) {
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
        List<Parameter> parameters = new ArrayList<>(keyValuePairs.values());
        parameters.sort(Comparator.comparingInt(Parameter::getOrder));
        return parameters;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
