package project.restapi.domain.models.api.response;

import java.util.Set;

public class CourseAllResponse {
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
}
