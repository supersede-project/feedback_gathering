package com.example.matthias.hostapplication.models;

import java.io.Serializable;

/**

 * Created by Matthias on 20.03.2016.
 */
public class FeedbackInvolved implements Serializable {
    private String username;
    private String firstname;
    private String lastname;
    private boolean isSuperuser;
    private int pk;
    private String email;

    public FeedbackInvolved() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public boolean isSuperuser() {
        return isSuperuser;
    }

    public void setIsSuperuser(boolean isSuperuser) {
        this.isSuperuser = isSuperuser;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
