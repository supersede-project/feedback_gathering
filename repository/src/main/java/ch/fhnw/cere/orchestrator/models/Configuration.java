package ch.fhnw.cere.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private TriggerType type;
    private Date createdAt;
    private Date updatedAt;
    @JsonIgnore
    @OneToMany(mappedBy = "configuration", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<ConfigurationMechanism> configurationMechanisms;

    @JsonIgnore
    @OneToMany(mappedBy = "configuration", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<ConfigurationUserGroup> configurationUserGroups;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "application_id")
    private Application application;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "general_configuration_id")
    private GeneralConfiguration generalConfiguration;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @Transient
    private ArrayList<Mechanism> mechanisms;

    public Configuration() {}

    public Configuration(String name, TriggerType type, Date createdAt, Date updatedAt, List<ConfigurationMechanism> configurationMechanisms, Application application) {
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.configurationMechanisms = configurationMechanisms;
        this.application = application;
    }

    public Configuration(String name, TriggerType type, List<ConfigurationMechanism> configurationMechanisms, Application application) {
        this.name = name;
        this.type = type;
        this.configurationMechanisms = configurationMechanisms;
        this.application = application;
    }

    public Configuration(String name, TriggerType type, Date createdAt, Date updatedAt, List<ConfigurationMechanism> configurationMechanisms, List<ConfigurationUserGroup> configurationUserGroups, Application application, GeneralConfiguration generalConfiguration, ArrayList<Mechanism> mechanisms) {
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.configurationMechanisms = configurationMechanisms;
        this.configurationUserGroups = configurationUserGroups;
        this.application = application;
        this.generalConfiguration = generalConfiguration;
        this.mechanisms = mechanisms;
    }

    @Override
    public String toString() {
        return String.format(
                "Configuration[id=%d, name='%s', type='%s', mechanisms='%s']",
                id, name, type, this.getMechanisms().stream().map(Object::toString)
                        .collect(Collectors.joining(", ")));
    }

    void filterByLanguage(String language, String fallbackLanguage) {
        for(Mechanism mechanism : this.getMechanisms()) {
            mechanism.setParameters(mechanism.parametersByLanguage(language, fallbackLanguage));
        }
    }

    public long getId() {
        return id;
    }

    public void setMechanisms(ArrayList<Mechanism> mechanisms) {
        if(this.configurationMechanisms == null) {
            this.configurationMechanisms = new ArrayList<ConfigurationMechanism>();
        }
        for(Mechanism mechanism : mechanisms) {
            ConfigurationMechanism configurationMechanism = new ConfigurationMechanism(this, mechanism, mechanism.isActive(), mechanism.getOrder(), new Date(), new Date());
            this.configurationMechanisms.add(configurationMechanism);
        }
    }

    public List<Mechanism> getMechanisms() {
        if (this.configurationMechanisms != null) {
            List<Mechanism> mechanisms = new ArrayList<>();
            this.configurationMechanisms.forEach(configurationMechanism -> {
                Mechanism mechanism = configurationMechanism.getMechanism();
                mechanism.setActive(configurationMechanism.isActive());
                mechanism.setOrder(configurationMechanism.getOrder());
                mechanisms.add(mechanism);
            });
            return mechanisms;
        }
        return new ArrayList<Mechanism>();
    }

    public void setUserGroups(ArrayList<UserGroup> userGroups) {
        if(this.configurationUserGroups == null) {
            this.configurationUserGroups = new ArrayList<ConfigurationUserGroup>();
        }
        for(UserGroup userGroup : userGroups) {
            ConfigurationUserGroup configurationUserGroup = new ConfigurationUserGroup(this, userGroup, userGroup.isActive(), new Date(), new Date());
            this.configurationUserGroups.add(configurationUserGroup);
        }
    }

    public List<UserGroup> getUserGroups() {
        if (this.configurationUserGroups != null) {
            List<UserGroup> userGroups = new ArrayList<>();
            this.configurationUserGroups.forEach(configurationUserGroup -> {
                UserGroup userGroup = configurationUserGroup.getUserGroup();
                userGroup.setActive(configurationUserGroup.isActive());
                userGroups.add(userGroup);
            });
            return userGroups;
        }
        return new ArrayList<UserGroup>();
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

    public List<ConfigurationUserGroup> getConfigurationUserGroups() {
        return configurationUserGroups;
    }

    public void setConfigurationUserGroups(List<ConfigurationUserGroup> configurationUserGroups) {
        this.configurationUserGroups = configurationUserGroups;
    }

    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    public void setGeneralConfiguration(GeneralConfiguration generalConfiguration) {
        this.generalConfiguration = generalConfiguration;
    }
}
