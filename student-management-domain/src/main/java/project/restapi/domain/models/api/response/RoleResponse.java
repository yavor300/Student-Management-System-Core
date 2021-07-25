package project.restapi.domain.models.api.response;

public class RoleResponse {
    private Long id;
    private String authority;

    public RoleResponse() {
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
