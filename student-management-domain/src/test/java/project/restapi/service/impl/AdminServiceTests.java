package project.restapi.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.restapi.domain.entities.Administrator;
import project.restapi.domain.entities.Role;
import project.restapi.domain.entities.Student;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.models.api.request.ChangeRoleStudentRequest;
import project.restapi.domain.models.api.request.ChangeRoleTeacherRequest;
import project.restapi.domain.models.api.response.ChangeRoleStudentResponse;
import project.restapi.domain.models.api.response.ChangeRoleTeacherResponse;
import project.restapi.exceptions.ObjectNotFoundException;
import project.restapi.repository.*;
import project.restapi.service.AdminService;
import project.restapi.service.RoleService;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class AdminServiceTests {
    private static final Long TEACHER_ID = 1L;
    private static final Long STUDENT_ID = 1L;

    private AdminService adminService;
    private AdministratorRepository mockAdministratorRepository;
    private TeacherRepository mockTeacherRepository;
    private RoleRepository mockRoleRepository;
    private RoleService mockRoleService;
    private StudentRepository mockStudentRepository;
    private CourseRepository mockCourseRepository;

    @Before
    public void init() {
        mockAdministratorRepository = Mockito.mock(AdministratorRepository.class);
        mockTeacherRepository = Mockito.mock(TeacherRepository.class);
        mockRoleRepository = Mockito.mock(RoleRepository.class);
        BCryptPasswordEncoder mockPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        mockRoleService = Mockito.mock(RoleService.class);
        mockStudentRepository = Mockito.mock(StudentRepository.class);
        mockCourseRepository = Mockito.mock(CourseRepository.class);


        adminService = new AdminServiceImpl(mockAdministratorRepository,mockTeacherRepository, mockRoleRepository,
                mockPasswordEncoder, mockRoleService, new ModelMapper(), mockStudentRepository, mockCourseRepository);
    }

    @Test
    public void seedAdmin_Should_Seed_Admin_Correctly() {
        Administrator administrator = new Administrator();

        Mockito.when(mockAdministratorRepository.count())
                .thenReturn(0L);

        Mockito.when(mockAdministratorRepository.saveAndFlush(any(Administrator.class)))
                .thenReturn(administrator);

        adminService.seedAdmin();

        Mockito.verify(mockAdministratorRepository, times(1)).saveAndFlush(any(Administrator.class));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void makeTeacherAdmin_Should_Throw_ObjectNotFoundException() {
        Mockito.when(mockTeacherRepository.findById(TEACHER_ID))
                .thenReturn(Optional.empty());

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(TEACHER_ID);

        adminService.makeTeacherAdmin(changeRoleTeacherRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void makeTeacherAdmin_Should_Throw_IllegalArgumentException_When_Role_Size_Is_One() {
        Teacher teacher = new Teacher();
        teacher.setAuthorities(Set.of(new Role()));

        Mockito.when(mockTeacherRepository.findById(TEACHER_ID))
                .thenReturn(Optional.of(teacher));

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(TEACHER_ID);

        adminService.makeTeacherAdmin(changeRoleTeacherRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void makeTeacherAdmin_Should_Throw_IllegalArgumentException_When_Role_Size_Is_Three() {
        Teacher teacher = new Teacher();
        teacher.setAuthorities(Set.of(new Role(), new Role() , new Role()));

        Mockito.when(mockTeacherRepository.findById(TEACHER_ID))
                .thenReturn(Optional.of(teacher));

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(TEACHER_ID);

        adminService.makeTeacherAdmin(changeRoleTeacherRequest);
    }

    @Test
    public void makeTeacherAdmin_Should_Work_Correctly() {
        Teacher teacher = new Teacher();
        teacher.getAuthorities().add(new Role());
        teacher.getAuthorities().add(new Role());

        Mockito.when(mockTeacherRepository.findById(TEACHER_ID))
                .thenReturn(Optional.of(teacher));

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(TEACHER_ID);

        Mockito.when(mockRoleService.getRolesForAdmin())
                .thenReturn(Set.of(new Role(), new Role(), new Role()));

        Mockito.when(mockTeacherRepository.saveAndFlush(any(Teacher.class)))
                .thenReturn(teacher);

        ChangeRoleTeacherResponse changeRoleTeacherResponse = adminService.makeTeacherAdmin(changeRoleTeacherRequest);

        Assert.assertEquals( 3, changeRoleTeacherResponse.getAuthorities().size());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void restoreTeacher_Should_Throw_ObjectNotFoundException() {
        Mockito.when(mockTeacherRepository.findById(TEACHER_ID))
                .thenReturn(Optional.empty());

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(TEACHER_ID);

        adminService.restoreTeacher(changeRoleTeacherRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void restoreTeacher_Should_Throw_IllegalArgumentException_When_Role_Size_Is_Less_Than_Three() {
        Teacher teacher = new Teacher();
        teacher.setAuthorities(Set.of(new Role()));

        Mockito.when(mockTeacherRepository.findById(TEACHER_ID))
                .thenReturn(Optional.of(teacher));

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(TEACHER_ID);

        adminService.restoreTeacher(changeRoleTeacherRequest);
    }

    @Test
    public void restoreTeacher_Should_Work_Correctly() {
        Teacher teacher = new Teacher();
        teacher.getAuthorities().add(new Role());
        teacher.getAuthorities().add(new Role());
        teacher.getAuthorities().add(new Role());

        Mockito.when(mockTeacherRepository.findById(TEACHER_ID))
                .thenReturn(Optional.of(teacher));

        Mockito.when(mockRoleRepository.findByAuthority("ROLE_STUDENT"))
                .thenReturn(new Role());

        Mockito.when(mockRoleRepository.findByAuthority("ROLE_TEACHER"))
                .thenReturn(new Role());

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(TEACHER_ID);

        Mockito.when(mockTeacherRepository.saveAndFlush(any(Teacher.class)))
                .thenReturn(teacher);

        ChangeRoleTeacherResponse changeRoleTeacherResponse = adminService.restoreTeacher(changeRoleTeacherRequest);

        Assert.assertEquals( 2, changeRoleTeacherResponse.getAuthorities().size());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void makeStudentTeacher_Should_Throw_ObjectNotFoundException() {
        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.empty());

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        adminService.makeStudentTeacher(changeRoleStudentRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void makeStudentTeacher_Should_Throw_IllegalArgumentException_When_Role_Size_Is_Two() {
        Student student = new Student();
        student.getAuthorities().add(new Role());
        student.getAuthorities().add(new Role());

        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(student));

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        adminService.makeStudentTeacher(changeRoleStudentRequest);
    }

    @Test
    public void makeStudentTeacher_Should_Work_Correctly() {
        Student student = new Student();
        student.getAuthorities().add(new Role());

        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(student));

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        Mockito.when(mockRoleRepository.findByAuthority("ROLE_STUDENT"))
                .thenReturn(new Role());

        Mockito.when(mockRoleRepository.findByAuthority("ROLE_TEACHER"))
                .thenReturn(new Role());

        Mockito.when(mockStudentRepository.saveAndFlush(any(Student.class)))
                .thenReturn(student);

        ChangeRoleStudentResponse changeRoleStudentResponse = adminService.makeStudentTeacher(changeRoleStudentRequest);

        Assert.assertEquals(2, changeRoleStudentResponse.getAuthorities().size());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void makeStudentAdmin_Should_Throw_An_ObjectNotFoundException() {
        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.empty());

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        adminService.makeStudentAdmin(changeRoleStudentRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void makeStudentAdmin_Should_Throw_IllegalArgumentException_When_Role_Size_Is_Three() {
        Student student = new Student();
        student.getAuthorities().add(new Role());
        student.getAuthorities().add(new Role());
        student.getAuthorities().add(new Role());

        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(student));

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        adminService.makeStudentAdmin(changeRoleStudentRequest);
    }

    @Test
    public void makeStudentAdmin_Should_Work_Correctly() {
        Student student = new Student();
        student.getAuthorities().add(new Role());

        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(student));

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        Mockito.when(mockRoleService.getRolesForAdmin())
                .thenReturn(Set.of(new Role(), new Role(), new Role()));

        Mockito.when(mockStudentRepository.saveAndFlush(any(Student.class)))
                .thenReturn(student);

        ChangeRoleStudentResponse changeRoleStudentResponse = adminService.makeStudentAdmin(changeRoleStudentRequest);

        Assert.assertEquals(3, changeRoleStudentResponse.getAuthorities().size());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void restoreStudent_Should_Throw_An_ObjectNotFoundException() {
        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.empty());

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        adminService.restoreStudent(changeRoleStudentRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void restoreStudent_Should_Throw_IllegalArgumentException_When_Role_Size_Is_One() {
        Student student = new Student();
        student.getAuthorities().add(new Role());

        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(student));

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        adminService.restoreStudent(changeRoleStudentRequest);
    }

    @Test
    public void restoreStudent_Should_Work_Correctly() {
        Student student = new Student();
        student.getAuthorities().add(new Role());
        student.getAuthorities().add(new Role());
        student.getAuthorities().add(new Role());

        Mockito.when(mockStudentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(student));

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(STUDENT_ID);

        Mockito.when(mockRoleRepository.findByAuthority("ROLE_STUDENT"))
                .thenReturn(new Role());

        Mockito.when(mockStudentRepository.saveAndFlush(any(Student.class)))
                .thenReturn(student);

        ChangeRoleStudentResponse changeRoleStudentResponse = adminService.restoreStudent(changeRoleStudentRequest);

        Assert.assertEquals(1, changeRoleStudentResponse.getAuthorities().size());
    }
}
