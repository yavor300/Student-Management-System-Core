package project.restapi.domain.models.api.response;

public class StudentProfileResponse {
    private Long id;
    private String name;
    private String username;
    private Integer age;
    private Integer coursesAttended;
    private Double averageGrade;

    public StudentProfileResponse() {
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCoursesAttended() {
        return coursesAttended;
    }

    public void setCoursesAttended(Integer coursesAttended) {
        this.coursesAttended = coursesAttended;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }
}
