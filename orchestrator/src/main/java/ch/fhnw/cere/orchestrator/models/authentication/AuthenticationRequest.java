package ch.fhnw.cere.orchestrator.models.authentication;


import java.io.Serializable;


public class AuthenticationRequest implements Serializable {

    private String name;
    private String password;

    public AuthenticationRequest() {
        super();
    }

    public AuthenticationRequest(String name, String password) {
        this.setName(name);
        this.setPassword(password);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}