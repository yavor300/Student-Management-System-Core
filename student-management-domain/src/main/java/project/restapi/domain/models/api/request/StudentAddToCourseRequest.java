package project.restapi.domain.models.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class StudentAddToCourseRequest {
    private Long studentId;
    private String courseName;

    public StudentAddToCourseRequest() {
    }

    @NotNull
    @Positive
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @NotNull
    @NotBlank
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
