package project.restapi.domain.models.api.response;

import java.util.Set;

public class ChangeRoleTeacherResponse {
    private Long teacherId;
    private Set<RoleResponse> authorities;

    public ChangeRoleTeacherResponse() {
    }

    public Long getEntityId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Set<RoleResponse> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<RoleResponse> authorities) {
        this.authorities = authorities;
    }
}
