package project.restapi.service;

import project.restapi.domain.models.api.request.StudentAddRequest;
import project.restapi.domain.models.api.request.StudentAddToCourseRequest;
import project.restapi.domain.models.api.response.CourseAllOrderedResponse;
import project.restapi.domain.models.api.response.StudentAddResponse;
import project.restapi.domain.models.api.response.StudentAddToCourseResponse;
import project.restapi.domain.models.api.response.StudentProfileResponse;

import java.util.List;

public interface StudentService {
    StudentAddResponse add(StudentAddRequest studentAddRequest);

    StudentAddToCourseResponse addToCourse(StudentAddToCourseRequest studentAddToCourseRequest);

    List<CourseAllOrderedResponse> getAllInCoursesAscByAverageGradeAsc();

    Double getTotalAverageGrade(String studentId);

    Double getTotalAverageGradeStudentSelf(Long studentId);

    StudentProfileResponse getProfileData(String name);
}
