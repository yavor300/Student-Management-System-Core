package project.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.restapi.constants.ApiPaths;
import project.restapi.domain.models.api.response.CourseAverageAll;
import project.restapi.domain.models.api.response.TeacherAllResponse;
import project.restapi.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.BASE_TEACHER)
public class TeacherRestController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping(ApiPaths.COURSES_AVERAGE_SELF)
    public ResponseEntity<List<CourseAverageAll>> getCoursesAllAverage() {
        return ResponseEntity.ok(teacherService.getCoursesAllAverage());
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping(ApiPaths.ALL_TEACHERS)
    public ResponseEntity<List<TeacherAllResponse>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAll());
    }


}

