package project.restapi.domain.models.api.response;

import java.util.List;

public class CourseAllOrderedResponse {
    private String name;
    private List<CourseAllOrderedStudentsOrderedResponse> students;

    public CourseAllOrderedResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseAllOrderedStudentsOrderedResponse> getStudents() {
        return students;
    }

    public void setStudents(List<CourseAllOrderedStudentsOrderedResponse> students) {
        this.students = students;
    }
}
