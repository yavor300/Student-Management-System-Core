package project.restapi.domain.models.api.response;

import java.util.List;

public class CourseAllOrderedStudentsOrderedResponse {
    private Long studentId;
    private String studentName;
    private Double gradeValue;
    List<Double> gradesGradeValue;

    public CourseAllOrderedStudentsOrderedResponse() {
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(Double gradeValue) {
        this.gradeValue = gradeValue;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public List<Double> getGradesGradeValue() {
        return gradesGradeValue;
    }

    public void setGradesGradeValue(List<Double> gradesGradeValue) {
        this.gradesGradeValue = gradesGradeValue;
    }
}
