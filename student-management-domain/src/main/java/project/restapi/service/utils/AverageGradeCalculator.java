package project.restapi.service.utils;

import project.restapi.constants.GradeValues;
import project.restapi.constants.OutputMessage;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.entities.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * The AverageGradeCalculator is used to calculate the average for a student in a given course.
 */
public class AverageGradeCalculator {
    public static Double getAverageGradeForAStudentInCourse(Student student, String courseName) {
        List<Double> grades = new ArrayList<>();

        for (Grade grade : student.getGrades()) {
            if (courseName.equals(grade.getCourse().getName())) {
                if (grade.getValue() >= GradeValues.MIN) {
                    grades.add(grade.getValue());
                }
            }
        }

        return Math.round(grades.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(OutputMessage.EMPTY_AVERAGE_GRADE) * 100.0) / 100.0;
    }
}
