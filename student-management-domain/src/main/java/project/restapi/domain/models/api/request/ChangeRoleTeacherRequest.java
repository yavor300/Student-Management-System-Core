package project.restapi.domain.models.api.request;

import com.sun.istack.NotNull;

import javax.validation.constraints.Positive;

public class ChangeRoleTeacherRequest {
    private Long teacherId;

    public ChangeRoleTeacherRequest() {
    }

    @NotNull
    @Positive
    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
