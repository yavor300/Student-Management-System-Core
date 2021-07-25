package project.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.restapi.constants.ApiPaths;
import project.restapi.domain.models.api.request.ChangeRoleStudentRequest;
import project.restapi.domain.models.api.request.ChangeRoleTeacherRequest;
import project.restapi.domain.models.api.request.RoleChangeRequest;
import project.restapi.domain.models.api.response.ChangeRoleStudentResponse;
import project.restapi.domain.models.api.response.ChangeRoleTeacherResponse;
import project.restapi.domain.models.api.response.RoleResponse;
import project.restapi.domain.models.api.response.UserAllResponse;
import project.restapi.service.AdminService;
import project.restapi.service.RoleService;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.BASE_ROLE)
public class RoleRestController {
    private final AdminService adminService;
    private final RoleService roleService;

    @Autowired
    public RoleRestController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiPaths.MAKE_TEACHER_ADMIN)
    public ResponseEntity<ChangeRoleTeacherResponse> makeTeacherAdmin(@RequestBody ChangeRoleTeacherRequest changeRoleTeacherRequest) {
        return ResponseEntity.ok(adminService.makeTeacherAdmin(changeRoleTeacherRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiPaths.RESTORE_TEACHER)
    public ResponseEntity<ChangeRoleTeacherResponse> restoreTeacher(@RequestBody ChangeRoleTeacherRequest changeRoleTeacherRequest) {
        return ResponseEntity.ok(adminService.restoreTeacher(changeRoleTeacherRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiPaths.MAKE_STUDENT_TEACHER)
    public ResponseEntity<ChangeRoleStudentResponse> makeStudentTeacher(@RequestBody ChangeRoleStudentRequest changeRoleStudentRequest) {
        return ResponseEntity.ok(adminService.makeStudentTeacher(changeRoleStudentRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiPaths.MAKE_STUDENT_ADMIN)
    public ResponseEntity<ChangeRoleStudentResponse> makeStudentAdmin(@RequestBody ChangeRoleStudentRequest changeRoleStudentRequest) {
        return ResponseEntity.ok(adminService.makeStudentAdmin(changeRoleStudentRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiPaths.RESTORE_STUDENT)
    public ResponseEntity<ChangeRoleStudentResponse> restoreStudent(@RequestBody ChangeRoleStudentRequest changeRoleStudentRequest) {
        return ResponseEntity.ok(adminService.restoreStudent(changeRoleStudentRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(ApiPaths.ALL_ROLES)
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiPaths.CHANGE_ROLE)
    public ResponseEntity<UserAllResponse> changeUserRoles(@RequestBody RoleChangeRequest roleChangeRequest) {
        return ResponseEntity.ok(adminService.changeUserRoles(roleChangeRequest));
    }
}
