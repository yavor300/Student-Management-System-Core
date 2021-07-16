package project.restapi.domain.models.api.request;

import project.restapi.constants.ErrorMessages;

import javax.validation.constraints.*;

public class GradeAddRequest {
    private Double value;
    private Long studentId;
    private String courseName;

    public GradeAddRequest() {
    }

    @Min(value = 2, message = ErrorMessages.GRADE_CANNOT_BE_LESS_THAN_TWO)
    @Max(value = 6, message = ErrorMessages.GRADE_CANNOT_BE_MORE_THAN_SIX)
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @NotNull
    @Positive
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @NotBlank(message = ErrorMessages.INVALID_NAME)
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}