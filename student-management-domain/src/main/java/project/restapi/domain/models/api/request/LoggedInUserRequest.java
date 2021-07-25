package project.restapi.domain.models.api.request;

public class LoggedInUserRequest {
    private String username;

    public LoggedInUserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
