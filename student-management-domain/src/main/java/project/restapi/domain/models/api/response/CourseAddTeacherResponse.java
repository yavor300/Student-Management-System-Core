package project.restapi.domain.models.api.response;

public class CourseAddTeacherResponse {
    private String name;
    private String teacherId;

    public CourseAddTeacherResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
