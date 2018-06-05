package ch.uzh.supersede.feedbacklibrary.models;

public class AuthenticateRequest {
    private String user;
    private String password;

    public AuthenticateRequest(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
