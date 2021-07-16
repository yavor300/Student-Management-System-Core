package project.restapi.domain.models.api.response;

import java.util.Set;

public class ChangeRoleStudentResponse {
    private Long studentId;
    private Set<RoleResponse> authorities;

    public ChangeRoleStudentResponse() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Set<RoleResponse> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<RoleResponse> authorities) {
        this.authorities = authorities;
    }
}
