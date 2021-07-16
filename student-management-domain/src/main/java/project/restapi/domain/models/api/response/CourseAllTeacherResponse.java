package project.restapi.domain.models.api.response;

import project.restapi.domain.entities.enums.Degree;

public class CourseAllTeacherResponse {
    private String name;
    private String username;
    private Degree degree;

    public CourseAllTeacherResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }
}
