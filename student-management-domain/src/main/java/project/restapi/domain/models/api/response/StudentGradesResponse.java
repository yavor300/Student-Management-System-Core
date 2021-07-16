package project.restapi.domain.models.api.response;

import java.util.List;

public class StudentGradesResponse {
    private String name;
    private List<GradeAllResponse> grades;

    public StudentGradesResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GradeAllResponse> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeAllResponse> grades) {
        this.grades = grades;
    }
}
