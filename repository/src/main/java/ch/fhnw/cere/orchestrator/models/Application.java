package ch.fhnw.cere.orchestrator.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private int state;
    private Date createdAt;
    private Date updatedAt;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "general_configuration_id")
    private GeneralConfiguration generalConfiguration;

    @OneToMany(mappedBy = "application", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Configuration> configurations;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public Application() {
    }

    public Application(String name, int state, Date createdAt, Date updatedAt, List<Configuration> configurations) {
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.configurations = configurations;
    }

    public Application(String name, int state, Date createdAt, Date updatedAt, GeneralConfiguration generalConfiguration, List<Configuration> configurations) {
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.generalConfiguration = generalConfiguration;
        this.configurations = configurations;
    }

    @Override
    public String toString() {
        return String.format(
                "Application[id=%d, name='%s', state='%d', generalConfiguration='%s', configurations='%s']",
                id, name, state,  this.generalConfiguration != null ? this.generalConfiguration : "null", this.configurations != null ? this.configurations.stream().map(Object::toString)
                        .collect(Collectors.joining(", ")): "null");
    }

    public void filterByLanguage(String language, String fallbackLanguage) {
        for(Configuration configuration: this.configurations) {
            configuration.filterByLanguage(language, fallbackLanguage);
        }
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    public void setGeneralConfiguration(GeneralConfiguration generalConfiguration) {
        this.generalConfiguration = generalConfiguration;
    }
}
