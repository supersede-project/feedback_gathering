package ch.fhnw.cere.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private TriggerType type;
    private Date createdAt;
    private Date updatedAt;
    @JsonIgnore
    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ConfigurationMechanism> configurationMechanisms;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="application_id")
    private Application application;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public Configuration() {}

    public Configuration(String name, TriggerType type, Date createdAt, Date updatedAt, List<ConfigurationMechanism> configurationMechanisms, Application application) {
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.configurationMechanisms = configurationMechanisms;
        this.application = application;
    }

    @Override
    public String toString() {
        return String.format(
                "Configuration[id=%d, name='%s', type='%s']",
                id, name, type);
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

    public TriggerType getType() {
        return type;
    }

    public void setType(TriggerType type) {
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

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public List<ConfigurationMechanism> getConfigurationMechanisms() {
        return configurationMechanisms;
    }

    public void setConfigurationMechanisms(List<ConfigurationMechanism> configurationMechanisms) {
        this.configurationMechanisms = configurationMechanisms;
    }

    public List<Mechanism> getMechanisms() {
        if (this.configurationMechanisms != null) {
            List<Mechanism> mechanisms = new ArrayList<>();
            this.configurationMechanisms.forEach(configurationMechanism -> mechanisms.add(configurationMechanism.getMechanism()));
            return mechanisms;
        }
        return null;
    }
}
