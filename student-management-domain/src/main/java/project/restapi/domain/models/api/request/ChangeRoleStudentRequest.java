package project.restapi.domain.models.api.request;

import com.sun.istack.NotNull;

import javax.validation.constraints.Positive;

public class ChangeRoleStudentRequest {
    private Long studentId;

    public ChangeRoleStudentRequest() {
    }

    @NotNull
    @Positive
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
