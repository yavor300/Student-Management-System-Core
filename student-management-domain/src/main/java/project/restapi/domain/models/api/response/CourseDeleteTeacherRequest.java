package project.restapi.domain.models.api.response;

public class CourseDeleteTeacherRequest {
    private String courseName;

    public CourseDeleteTeacherRequest() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
