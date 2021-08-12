package project.restapi.service;

import project.restapi.domain.models.api.request.TeacherAddRequest;
import project.restapi.domain.models.api.response.CourseAverageAll;
import project.restapi.domain.models.api.response.TeacherAddResponse;
import project.restapi.domain.models.api.response.TeacherAllResponse;

import java.util.List;

public interface TeacherService {
    TeacherAddResponse add(TeacherAddRequest teacherAddRequest);

    List<CourseAverageAll> getCoursesAllAverage();

    List<TeacherAllResponse> getAll();

    void seedTeachers();
}
