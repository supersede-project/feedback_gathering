package ch.uzh.supersede.feedbacklibrary.models;

public class AuthenticateRequest {
    private String name;
    private String password;

    public AuthenticateRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
