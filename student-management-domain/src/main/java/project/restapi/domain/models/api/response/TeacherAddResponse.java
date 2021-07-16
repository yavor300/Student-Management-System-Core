package project.restapi.domain.models.api.response;

import project.restapi.domain.entities.enums.Degree;

public class TeacherAddResponse {
    private Long id;
    private String name;
    private String username;
    private Degree degree;

    public TeacherAddResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
