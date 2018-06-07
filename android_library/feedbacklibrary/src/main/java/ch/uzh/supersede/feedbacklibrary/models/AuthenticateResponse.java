package ch.uzh.supersede.feedbacklibrary.models;

public class AuthenticateResponse {
    private String token;

    public AuthenticateResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
