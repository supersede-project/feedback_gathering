package ch.fhnw.cere.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;


@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(mappedBy = "userGroup", fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private List<User> users;

    @Transient
    private boolean active;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "application_id")
    private Application application;

    public UserGroup() {
    }

    public UserGroup(String name, List<User> users, Application application) {
        this.name = name;
        this.users = users;
        this.application = application;
    }

    public boolean containsUserWithUserIdentification(String userIdentification) {
        return this.getUsers() != null && this.getUsers().stream().filter(user -> user.getUserIdentification().equals(userIdentification)).count() > 0;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
