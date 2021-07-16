package project.restapi.domain.models.api.response;

public class CourseAllStudentResponse {
    private String name;
    private Integer age;
    private String username;

    public CourseAllStudentResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
