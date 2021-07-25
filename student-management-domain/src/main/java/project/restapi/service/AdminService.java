package project.restapi.service;

import project.restapi.domain.models.api.request.ChangeRoleStudentRequest;
import project.restapi.domain.models.api.request.ChangeRoleTeacherRequest;
import project.restapi.domain.models.api.request.RoleChangeRequest;
import project.restapi.domain.models.api.response.ChangeRoleStudentResponse;
import project.restapi.domain.models.api.response.ChangeRoleTeacherResponse;
import project.restapi.domain.models.api.response.CourseAllWithTeacherAndAverageResponse;
import project.restapi.domain.models.api.response.UserAllResponse;

import java.util.List;

public interface AdminService {
    void seedAdmin();

    ChangeRoleTeacherResponse makeTeacherAdmin(ChangeRoleTeacherRequest changeRoleTeacherRequest);

    ChangeRoleTeacherResponse restoreTeacher(ChangeRoleTeacherRequest changeRoleTeacherRequest);

    ChangeRoleStudentResponse makeStudentTeacher(ChangeRoleStudentRequest changeRoleStudentRequest);

    ChangeRoleStudentResponse makeStudentAdmin(ChangeRoleStudentRequest changeRoleStudentRequest);

    ChangeRoleStudentResponse restoreStudent(ChangeRoleStudentRequest changeRoleStudentRequest);

    List<CourseAllWithTeacherAndAverageResponse> getAllCoursesWithTeachersAndAverage();

    List<UserAllResponse> getAllUsers();

    UserAllResponse changeUserRoles(RoleChangeRequest roleChangeRequest);
}
