package ch.fhnw.cere.repository.models.authentication;


import java.io.Serializable;


public class AuthenticationResponse implements Serializable {

    private String token;

    public AuthenticationResponse() {
        super();
    }

    public AuthenticationResponse(String token) {
        this.setToken(token);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}