package ch.fhnw.cere.orchestrator.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.stream.Collectors;


@Entity
@Table(name = "configuration_mechanism")
public class ConfigurationMechanism {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private Configuration configuration;
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch=FetchType.EAGER)
    @JoinColumn(name = "mechanism_id")
    private Mechanism mechanism;
    private boolean active;
    @Column(name="`order`")
    private int order;
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public ConfigurationMechanism() {
    }

    public ConfigurationMechanism(Configuration configuration, Mechanism mechanism, boolean active, int order, Date createdAt, Date updatedAt) {
        this.configuration = configuration;
        this.mechanism = mechanism;
        this.active = active;
        this.order = order;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ConfigurationMechanism(Configuration configuration, Mechanism mechanism, boolean active, int order) {
        this.configuration = configuration;
        this.mechanism = mechanism;
        this.active = active;
        this.order = order;
    }

    public ConfigurationMechanism(Configuration configuration, boolean active, int order) {
        this.configuration = configuration;
        this.active = active;
        this.order = order;
    }

    @Override
    public String toString() {
        return String.format(
                "ConfigurationMechanism[id=%d, active='%b', order='%d', configuration.id='%d', mechanism.id='%d']",
                id, active, order, this.configuration == null ? 0 : this.configuration.getId(), this.mechanism == null ? 0 : this.mechanism.getId());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Mechanism getMechanism() {
        return mechanism;
    }

    public void setMechanism(Mechanism mechanism) {
        this.mechanism = mechanism;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
}



