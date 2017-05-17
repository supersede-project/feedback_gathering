package ch.fhnw.cere.orchestrator.models;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Mechanism {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private MechanismType type;
    private Date createdAt;
    private Date updatedAt;
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

    @Override
    public String toString() {
        return String.format(
                "Mechanism[id=%d, type='%s']",
                id, type);
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


