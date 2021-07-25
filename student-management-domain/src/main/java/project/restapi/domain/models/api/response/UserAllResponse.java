package project.restapi.domain.models.api.response;

import project.restapi.domain.entities.Role;

import java.util.Set;

public class UserAllResponse {
    private Long id;
    private String username;
    private Set<Role> authorities;

    public UserAllResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }
}
