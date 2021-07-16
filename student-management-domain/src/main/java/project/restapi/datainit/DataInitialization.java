package project.restapi.datainit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.restapi.service.AdminService;
import project.restapi.service.CourseService;
import project.restapi.service.RoleService;

@Component
public class DataInitialization implements CommandLineRunner {
    private final CourseService courseService;
    private final RoleService roleService;
    private final AdminService adminService;

    @Autowired
    public DataInitialization(CourseService courseService, RoleService roleService, AdminService adminService) {
        this.courseService = courseService;
        this.roleService = roleService;
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) {
        courseService.seedCourses();
        roleService.seedRoles();
        adminService.seedAdmin();
    }
}