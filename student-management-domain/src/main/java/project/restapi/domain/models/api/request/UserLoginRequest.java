package project.restapi.domain.models.api.request;

import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;

public class UserLoginRequest {
    private String username;
    private String password;

    public UserLoginRequest() {
    }

    @NotNull
    @NotBlank
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @NotBlank
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
