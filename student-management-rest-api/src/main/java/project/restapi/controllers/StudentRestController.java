package project.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.restapi.constants.ApiPaths;
import project.restapi.domain.entities.Student;
import project.restapi.domain.models.api.request.CourseAvailableStudentsRequest;
import project.restapi.domain.models.api.request.StudentAddToCourseRequest;
import project.restapi.domain.models.api.response.StudentAddToCourseResponse;
import project.restapi.domain.models.api.response.StudentAvailableResponse;
import project.restapi.domain.models.api.response.StudentProfileResponse;
import project.restapi.service.StudentService;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.BASE_STUDENT)
public class StudentRestController {
    private final StudentService studentService;

    @Autowired
    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(ApiPaths.STUDENT_AVERAGE)
    public ResponseEntity<Double> getTotalAverage(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getTotalAverageGrade(id));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @CrossOrigin
    @GetMapping(ApiPaths.STUDENT_AVERAGE_SELF)
    public ResponseEntity<Double> getTotalAverageSelf() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Student student = (Student) authentication.getPrincipal();
        return ResponseEntity.ok(studentService.getTotalAverageGradeStudentSelf(student.getId()));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping(ApiPaths.STUDENT_ADD_COURSE)
    public ResponseEntity<StudentAddToCourseResponse> addToCourse(@RequestBody StudentAddToCourseRequest studentAddToCourseRequest) {
        return ResponseEntity.ok(studentService.addToCourse(studentAddToCourseRequest));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @CrossOrigin
    @GetMapping(ApiPaths.STUDENT_PROFILE)
    public ResponseEntity<StudentProfileResponse> getProfileData(@PathVariable String name) {
        return ResponseEntity.ok(studentService.getProfileData(name));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @CrossOrigin
    @PostMapping("/available")
    public ResponseEntity<List<StudentAvailableResponse>> getStudentsNotInCourse(@RequestBody CourseAvailableStudentsRequest courseAvailableStudentsRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.studentService.getStudentsNotInCourse(courseAvailableStudentsRequest));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/get/{id}")
    public ResponseEntity<StudentProfileResponse> getStudentByName(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }
}