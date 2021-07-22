package project.restapi.domain.models.api.mapping;

import org.modelmapper.ModelMapper;
import project.restapi.constants.GradeValues;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.models.api.response.CourseAllOrderedResponse;
import project.restapi.domain.models.api.response.CourseAllOrderedStudentsOrderedResponse;

import java.util.ArrayList;

import static project.restapi.service.utils.AverageGradeCalculator.getAverageGradeForAStudentInCourse;

public class CourseAndStudentsOrderedMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static CourseAllOrderedResponse mapCourseAndStudents(Course course) {
        CourseAllOrderedResponse courseAllOrderedResponse =
                modelMapper.map(course, CourseAllOrderedResponse.class);
        courseAllOrderedResponse.setStudents(new ArrayList<>());

        double courseAverageGrade = course.getGrades()
                .stream()
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(GradeValues.EMPTY_AVERAGE_GRADE);

        courseAllOrderedResponse.setAverageGrade(courseAverageGrade);

        course.getStudents().forEach(student -> {
            CourseAllOrderedStudentsOrderedResponse studentsOrderedByAverageGradeAscRestModel
                    = new CourseAllOrderedStudentsOrderedResponse();
            studentsOrderedByAverageGradeAscRestModel.setStudentName(student.getName());
            studentsOrderedByAverageGradeAscRestModel.setGradeValue(
                    getAverageGradeForAStudentInCourse(student, course.getName()));
            courseAllOrderedResponse.getStudents().add(studentsOrderedByAverageGradeAscRestModel);
        });

        return courseAllOrderedResponse;
    }
}
