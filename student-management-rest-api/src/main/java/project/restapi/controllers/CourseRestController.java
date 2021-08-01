package project.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.restapi.constants.ApiPaths;
import project.restapi.domain.models.api.mapping.CourseAndStudentsOrderedMapper;
import project.restapi.domain.models.api.request.CourseAddRequest;
import project.restapi.domain.models.api.request.CourseAddTeacherRequest;
import project.restapi.domain.models.api.response.*;
import project.restapi.service.CourseService;
import project.restapi.service.StudentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiPaths.BASE_COURSE)
public class CourseRestController {
    private final CourseService courseService;
    private final StudentService studentService;

    @Autowired
    public CourseRestController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @CrossOrigin
    @GetMapping(ApiPaths.COURSE_ALL)
    public ResponseEntity<List<CourseAllResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping(ApiPaths.COURSE_ALL_ORDERED)
    public ResponseEntity<List<CourseAllOrderedResponse>> getAllCoursesOrdered() {
        return ResponseEntity.ok(studentService.getAllInCoursesAscByAverageGradeAsc());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(ApiPaths.COURSE_AVERAGE)
    public ResponseEntity<CourseAllOrderedResponse> getAverageInCourse(@PathVariable Long id) {
        return ResponseEntity.ok(CourseAndStudentsOrderedMapper.mapCourseAndStudents(courseService.getByName(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<CourseAddResponse> addCourse(@RequestBody @Valid CourseAddRequest courseAddRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(courseService.add(courseAddRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiPaths.COURSE_ADD_TEACHER)
    public ResponseEntity<CourseAddTeacherResponse> addTeacher(@RequestBody CourseAddTeacherRequest courseAddTeacherRequest) {
        return ResponseEntity.ok(courseService.addTeacher(courseAddTeacherRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiPaths.COURSE_DELETE_TEACHER)
    public ResponseEntity<CourseAddTeacherResponse> deleteTeacher(@RequestBody CourseDeleteTeacherRequest courseDeleteTeacherRequest) {
        return ResponseEntity.ok(courseService.deleteTeacher(courseDeleteTeacherRequest));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(ApiPaths.GET_TEACHER)
    public ResponseEntity<CourseTeacherResponse> addTeacher(@PathVariable String name) {
        return ResponseEntity.ok(courseService.getTeacher(name));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(ApiPaths.ALL_STUDENTS)
    public ResponseEntity<CourseAllResponse> getAllStudents(@PathVariable String name) {
        return ResponseEntity.ok(courseService.getAllStudents(name));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping(ApiPaths.STUDENTS_GRADES)
    public ResponseEntity<CourseStudentGradesResponse> getAllStudentsGrades(@PathVariable String name) {
        return ResponseEntity.ok(courseService.getAllStudentsGrades(name));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping(ApiPaths.COURSE_AVERAGE_GRADE)
    public ResponseEntity<Double> getAverageGrade(@PathVariable String name) {
        return ResponseEntity.ok(courseService.getAverageGrade(name));
    }
}