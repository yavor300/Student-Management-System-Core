package project.restapi.domain.models.api.request;

import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class CourseAddTeacherRequest {
    private Long teacherId;
    private String courseName;

    public CourseAddTeacherRequest() {
    }

    @NotNull
    @Positive
    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    @NotBlank
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
