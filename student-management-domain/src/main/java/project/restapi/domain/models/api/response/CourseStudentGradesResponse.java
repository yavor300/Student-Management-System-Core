package project.restapi.domain.models.api.response;

import java.util.List;

public class CourseStudentGradesResponse {
    private String name;
    private List<StudentGradesResponse> students;

    public CourseStudentGradesResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentGradesResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentGradesResponse> students) {
        this.students = students;
    }
}
