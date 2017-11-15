package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
public class ApiUserApiUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "api_user_id")
    @JsonIgnore
    private ApiUser apiUser;

    private ApiUserRole apiUserRole;

    public ApiUserApiUserRole() {
    }

    public ApiUserApiUserRole(ApiUser apiUser, ApiUserRole apiUserRole) {
        this.apiUser = apiUser;
        this.apiUserRole = apiUserRole;
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

    public ApiUserRole getApiUserRole() {
        return apiUserRole;
    }

    public void setApiUserRole(ApiUserRole apiUserRole) {
        this.apiUserRole = apiUserRole;
    }
}