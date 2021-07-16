package project.restapi.domain.models.api.response;

public class CourseAverageAll {
    private String name;
    private Double averageGrade;

    public CourseAverageAll() {
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
}
