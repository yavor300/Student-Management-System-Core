package project.restapi.domain.models.api.response;

public class StudentAddToCourseResponse {
    private Long studentId;
    private String courseName;

    public StudentAddToCourseResponse() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
