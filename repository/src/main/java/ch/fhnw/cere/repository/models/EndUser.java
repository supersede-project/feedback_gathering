package ch.fhnw.cere.repository.models;

import scala.xml.Null;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Aydinli on 08.11.2017.
 */
@Entity
public class EndUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date createdAt;
    private Date updatedAt;
    private long applicationId;
    private String username;
    private String email;
    /**
     * Default NULL
     */
    @Column(name = "phone_number")
    private int phoneNumber;



    @OneToMany(mappedBy = "enduser", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<UserFBDislike> userFBDislikes;

    @OneToMany(mappedBy = "enduser", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<UserFBLike> userFBLikes;

    public EndUser() {
    }

    public EndUser(Date createdAt, Date updatedAt, long applicationId, String username, int phoneNumber) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.applicationId = applicationId;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public EndUser(long applicationId, String username) {
        this.applicationId = applicationId;
        this.username = username;
    }

    public EndUser(long applicationId, String username, int phoneNumber) {
        this.applicationId = applicationId;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public EndUser(long applicationId, String username, int phoneNumber, String email) {
        this.applicationId = applicationId;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @Override
    public String toString() {
        return String.format(
                "Enduser[id=%d, username=%s]",
                id, username);
    }

}
