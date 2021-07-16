package project.restapi.domain.models.api.response;

public class CourseAllOrderedStudentsOrderedResponse {
    private String studentName;
    private Double gradeValue;

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
}
