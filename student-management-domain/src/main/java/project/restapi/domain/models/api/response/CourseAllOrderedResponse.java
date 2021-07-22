package project.restapi.domain.models.api.response;

import java.util.List;

public class CourseAllOrderedResponse {
    private Long id;
    private String name;
    private Double totalHours;
    private String teacherName;
    private Double averageGrade;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }
}
