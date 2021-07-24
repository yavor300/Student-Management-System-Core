package project.restapi.service;

import project.restapi.domain.models.api.request.CourseAvailableStudentsRequest;
import project.restapi.domain.models.api.request.StudentAddRequest;
import project.restapi.domain.models.api.request.StudentAddToCourseRequest;
import project.restapi.domain.models.api.response.*;

import java.util.List;

public interface StudentService {
    StudentAddResponse add(StudentAddRequest studentAddRequest);

    StudentAddToCourseResponse addToCourse(StudentAddToCourseRequest studentAddToCourseRequest);

    List<CourseAllOrderedResponse> getAllInCoursesAscByAverageGradeAsc();

    Double getTotalAverageGrade(String studentId);

    Double getTotalAverageGradeStudentSelf(Long studentId);

    StudentProfileResponse getProfileData(String name);

    List<StudentAvailableResponse> getStudentsNotInCourse(CourseAvailableStudentsRequest courseAvailableStudentsRequest);
}
