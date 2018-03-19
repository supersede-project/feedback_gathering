package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
public class ApiUserPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "api_user_id")
    private ApiUser apiUser;

    private long applicationId;

    private boolean hasPermission;

    public ApiUserPermission() {
    }

    public ApiUserPermission(ApiUser apiUser, long applicationId, boolean hasPermission) {
        this.apiUser = apiUser;
        this.applicationId = applicationId;
        this.hasPermission = hasPermission;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApiUser getApiUser() {
        return apiUser;
    }

    public void setApiUser(ApiUser apiUser) {
        this.apiUser = apiUser;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public boolean hasPermission() {
        return hasPermission;
    }

    public void setPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }
}