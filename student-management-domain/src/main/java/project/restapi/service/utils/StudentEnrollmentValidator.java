package project.restapi.service.utils;

import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Student;

import java.util.Optional;

/**
 * The StudentEnrollmentValidator class checks whether a given student is signed in a given course.
 */
public class StudentEnrollmentValidator {
    public static boolean isStudentInCourse(Student student, Course course) {
        Optional<Course> optionalCourse = student.getCourses()
                .stream()
                .filter(c -> c.getName().equals(course.getName()))
                .findFirst();

        return optionalCourse.isPresent();
    }
}
