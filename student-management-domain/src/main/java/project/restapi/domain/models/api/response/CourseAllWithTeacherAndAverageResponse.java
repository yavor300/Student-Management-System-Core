package project.restapi.domain.models.api.response;

public class CourseAllWithTeacherAndAverageResponse {
    private String name;
    private Double averageGrade;
    private String teacherName;

    public CourseAllWithTeacherAndAverageResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
