package project.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.restapi.constants.ApiPaths;
import project.restapi.domain.models.api.response.CourseAllWithTeacherAndAverageResponse;
import project.restapi.domain.models.api.response.UserAllResponse;
import project.restapi.service.AdminService;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.BASE_ADMIN)
public class AdminRestController {
    private final AdminService adminService;

    @Autowired
    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(ApiPaths.COURSES_AVERAGE_SELF)
    public ResponseEntity<List<CourseAllWithTeacherAndAverageResponse>> getAllCoursesWithTeachersAndAverage() {
        return ResponseEntity.ok(adminService.getAllCoursesWithTeachersAndAverage());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(ApiPaths.ALL_USERS)
    public ResponseEntity<List<UserAllResponse>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.adminService.getAllUsers());
    }
}
