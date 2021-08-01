package project.restapi.service;

import project.restapi.domain.entities.Course;
import project.restapi.domain.models.api.request.CourseAddRequest;
import project.restapi.domain.models.api.request.CourseAddTeacherRequest;
import project.restapi.domain.models.api.response.*;

import java.util.List;

public interface CourseService {
    CourseAddResponse add(CourseAddRequest courseAddRequest);

    CourseAddTeacherResponse addTeacher(CourseAddTeacherRequest courseAddTeacherRequest);

    Course getByName(Long id);

    List<CourseAllResponse> getAllCourses();

    void seedCourses();

    CourseTeacherResponse getTeacher(String courseName);

    CourseAllResponse getAllStudents(String courseName);

    CourseStudentGradesResponse getAllStudentsGrades(String courseName);

    Double getAverageGrade(String courseName);

    CourseAddTeacherResponse deleteTeacher(CourseDeleteTeacherRequest courseDeleteTeacherRequest);
}
