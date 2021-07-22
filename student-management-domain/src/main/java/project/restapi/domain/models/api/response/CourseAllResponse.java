package project.restapi.domain.models.api.response;

import java.util.Set;

public class CourseAllResponse {
    private Long id;
    private String teacherName;
    private Double averageGrade;
    private String name;
    private Double totalHours;
    private Set<CourseAllStudentResponse> students;
    private CourseAllTeacherResponse teacher;

    public CourseAllResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Set<CourseAllStudentResponse> getStudents() {
        return students;
    }

    public void setStudents(Set<CourseAllStudentResponse> students) {
        this.students = students;
    }

    public CourseAllTeacherResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(CourseAllTeacherResponse teacher) {
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
