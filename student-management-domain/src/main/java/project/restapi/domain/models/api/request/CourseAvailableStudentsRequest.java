package project.restapi.domain.models.api.request;

public class CourseAvailableStudentsRequest {
    private String courseName;

    public CourseAvailableStudentsRequest() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
