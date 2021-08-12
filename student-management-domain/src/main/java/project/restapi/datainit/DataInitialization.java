package project.restapi.datainit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.restapi.service.*;

@Component
public class DataInitialization implements CommandLineRunner {
    private final CourseService courseService;
    private final RoleService roleService;
    private final AdminService adminService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    @Autowired
    public DataInitialization(CourseService courseService, RoleService roleService, AdminService adminService, TeacherService teacherService, StudentService studentService) {
        this.courseService = courseService;
        this.roleService = roleService;
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @Override
    public void run(String... args) {
        courseService.seedCourses();
        roleService.seedRoles();
        adminService.seedAdmin();
        teacherService.seedTeachers();
        studentService.seedStudents();
    }
}