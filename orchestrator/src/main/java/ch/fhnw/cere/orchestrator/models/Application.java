package ch.fhnw.cere.orchestrator.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @OneToMany(mappedBy = "application", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<User> users;

    @OneToMany(mappedBy = "application", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<UserGroup> userGroups;

    @OneToMany(mappedBy = "application", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<ApiUserPermission> apiUserPermissions;

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

    public Application(String name, int state, Date createdAt, Date updatedAt, GeneralConfiguration generalConfiguration, List<Configuration> configurations, List<User> users) {
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.generalConfiguration = generalConfiguration;
        this.configurations = configurations;
        this.users = users;
    }

    public Application(String name, int state, Date createdAt, Date updatedAt, GeneralConfiguration generalConfiguration, List<Configuration> configurations, List<User> users, List<UserGroup> userGroups) {
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.generalConfiguration = generalConfiguration;
        this.configurations = configurations;
        this.users = users;
        this.userGroups = userGroups;
    }

    public Application(String name, int state, Date createdAt, Date updatedAt, GeneralConfiguration generalConfiguration, List<Configuration> configurations, List<User> users, List<UserGroup> userGroups, List<ApiUserPermission> apiUserPermissions) {
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.generalConfiguration = generalConfiguration;
        this.configurations = configurations;
        this.users = users;
        this.userGroups = userGroups;
        this.apiUserPermissions = apiUserPermissions;
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
        if(this.generalConfiguration != null) {
            generalConfiguration.setParameters(generalConfiguration.parametersByLanguage(language, fallbackLanguage));
        }
    }

    /**
     * Filters the applications's configurations for a user that is not yet known by the orchestrator. That means the
     * user gets the push configuration that is not user group specific and the default pull configurations.
     *
     * @return Application
     */
    public Application filterForAnonymousUser() {
        this.configurations = this.configurations.stream().filter(configuration ->
                configuration.isPushDefault() || configuration.isPullDefault()
        ).collect(Collectors.toList());
        return this;
    }

    /**
     * Filters the applications's configurations for a certain user identification. That means the user gets the
     * configuration for each type tailored to this user identification or user group belonging. If the user does not
     * belong to a group or there is no user assignment for a type of configuration, the default configuration for this
     * type of configuration is returned. That means if a pull configuration is assigned to the user, but not a push
     * configuration, the user still get's the default push configuration.
     *
     * @param userIdentification which is the user ID that is passed to the client side component and stored alongside
     *                           the feedback the user submitted.
     * @return Application
     */
    public Application filterByUserIdentification(String userIdentification) {
        List<Configuration> pushConfigurationsForUser = this.configurations.stream().filter(configuration ->
                        configuration.getType().equals(TriggerType.PUSH) &&
                        configuration.containsUserWithUserIdentification(userIdentification)
        ).collect(Collectors.toList());

        List<Configuration> pullConfigurationsForUser = this.configurations.stream().filter(configuration ->
                        configuration.getType().equals(TriggerType.PULL) &&
                        configuration.containsUserWithUserIdentification(userIdentification)
        ).collect(Collectors.toList());

        if(pushConfigurationsForUser.size() == 0) {
            pushConfigurationsForUser.add(this.getDefaultConfigurationForTriggerType(TriggerType.PUSH));
        }
        if(pullConfigurationsForUser.size() == 0) {
            pullConfigurationsForUser.add(this.getDefaultConfigurationForTriggerType(TriggerType.PULL));
        }

        this.configurations = new ArrayList<Configuration>() {{
            addAll(pushConfigurationsForUser);
            addAll(pullConfigurationsForUser);
        }};
        return this;
    }

    /**
     * Filters the applications's configurations for a certain user group. That means the users get the
     * user group push configuration if any (or the default push) and the pull configurations their group is assigned to.
     * If no pull configuration is assigned to their group, they do not get any pull configuration at all.
     *
     * @param userGroupId id of the user group
     * @return Application
     */
    public Application filterByUserGroupId(long userGroupId) {
        this.configurations = this.configurations.stream().filter(configuration ->
                configuration.containsUserGroupWithId(userGroupId)
        ).collect(Collectors.toList());
        return this;
    }

    public Configuration getDefaultConfigurationForTriggerType(TriggerType triggerType) {
        List<Configuration> defaultConfigurations;
        if(triggerType.equals(TriggerType.PUSH)) {
             defaultConfigurations = this.configurations.stream().filter(configuration ->
                    configuration.isPushDefault()).collect(Collectors.toList());
        } else {
            defaultConfigurations = this.configurations.stream().filter(configuration ->
                    configuration.isPullDefault()).collect(Collectors.toList());
        }
        if(defaultConfigurations == null || defaultConfigurations.size() < 1) {
            return null;
        }
        return defaultConfigurations.get(0);
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public List<ApiUserPermission> getApiUserPermissions() {
        return apiUserPermissions;
    }

    public void setApiUserPermissions(List<ApiUserPermission> apiUserPermissions) {
        this.apiUserPermissions = apiUserPermissions;
    }
}
