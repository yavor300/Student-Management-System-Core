package project.restapi.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.entities.Role;
import project.restapi.domain.entities.Student;
import project.restapi.domain.models.api.request.StudentAddRequest;
import project.restapi.domain.models.api.request.StudentAddToCourseRequest;
import project.restapi.domain.models.api.response.CourseAllOrderedResponse;
import project.restapi.domain.models.api.response.StudentAddResponse;
import project.restapi.domain.models.api.response.StudentAddToCourseResponse;
import project.restapi.exceptions.ObjectAlreadyExistsException;
import project.restapi.exceptions.ObjectNotFoundException;
import project.restapi.repository.CourseRepository;
import project.restapi.repository.RoleRepository;
import project.restapi.repository.StudentRepository;
import project.restapi.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class StudentServiceTests {
    public static final Long ID = 1L;
    public static final String INVALID_NAME = "AA";
    public static final String NAME = "Name";
    public static final String INVALID_UCN = "990122";
    public static final String UCN = "9901220982";
    public static final String USERNAME = "john";
    public static final String BLANK_PASSWORD = "";
    public static final String PASSWORD = "password1234";
    public static final String ROLE_STUDENT = "ROLE_STUDENT";
    private static final String COURSE_NAME = "Java";
    private static final Double EXCELLENT_GRADE = 6.0;
    private static final Double GOOD_GRADE = 4.0;


    private StudentRepository mockStudentRepository;
    private CourseRepository mockCourseRepository;
    private RoleRepository mockRoleRepository;
    private BCryptPasswordEncoder mockPasswordEncoder;
    private StudentService studentService;

    @Before
    public void init() {
        mockStudentRepository = Mockito.mock(StudentRepository.class);
        mockCourseRepository = Mockito.mock(CourseRepository.class);
        mockRoleRepository = Mockito.mock(RoleRepository.class);
        mockPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        studentService = new StudentServiceImpl(mockStudentRepository, mockCourseRepository, mockRoleRepository,
                mockPasswordEncoder, new ModelMapper());
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_Should_Throw_IllegalArgumentException_When_Name_Invalid() {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setName(INVALID_NAME);

        studentService.add(studentAddRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_Should_Throw_IllegalArgumentException_When_UCN_Invalid() {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setName(NAME);
        studentAddRequest.setUniqueCitizenNumber(INVALID_UCN);

        studentService.add(studentAddRequest);
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void add_Should_Throw_ObjectAlreadyExistsException_When_Student_Exists_Based_On_UCN() {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setName(NAME);
        studentAddRequest.setUniqueCitizenNumber(UCN);

        Mockito.when(mockStudentRepository.findByUniqueCitizenNumber(UCN))
                .thenReturn(Optional.of(new Student()));

        studentService.add(studentAddRequest);
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void add_Should_Throw_ObjectAlreadyExistsException_When_Student_Exists_Based_On_Username() {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setName(NAME);
        studentAddRequest.setUniqueCitizenNumber(UCN);
        studentAddRequest.setUsername(USERNAME);

        Mockito.when(mockStudentRepository.findByUniqueCitizenNumber(UCN))
                .thenReturn(Optional.empty());

        Mockito.when(mockStudentRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(new Student()));

        studentService.add(studentAddRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_Should_Throw_IllegalArgumentException_When_Password_Is_Blank() {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setName(NAME);
        studentAddRequest.setUniqueCitizenNumber(UCN);
        studentAddRequest.setUsername(USERNAME);
        studentAddRequest.setPassword(BLANK_PASSWORD);

        Mockito.when(mockStudentRepository.findByUniqueCitizenNumber(UCN))
                .thenReturn(Optional.empty());

        Mockito.when(mockStudentRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        studentService.add(studentAddRequest);
    }

    @Test
    public void add_Should_Add_User_Successfully() {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setName(NAME);
        studentAddRequest.setUniqueCitizenNumber(UCN);
        studentAddRequest.setUsername(USERNAME);
        studentAddRequest.setPassword(PASSWORD);

        Mockito.when(mockStudentRepository.findByUniqueCitizenNumber(UCN))
                .thenReturn(Optional.empty());

        Mockito.when(mockStudentRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        Mockito.when(mockRoleRepository.findByAuthority(ROLE_STUDENT))
                .thenReturn(new Role(ROLE_STUDENT));

        Mockito.when(mockPasswordEncoder.encode(PASSWORD))
                .thenReturn(PASSWORD);

        Student student = new Student();
        student.setId(ID);
        student.setUsername(USERNAME);
        student.setName(NAME);
        Mockito.when(mockStudentRepository.saveAndFlush(any(Student.class)))
                .thenReturn(student);

        StudentAddResponse studentAddResponse = studentService.add(studentAddRequest);

        Assert.assertEquals(ID, studentAddResponse.getId());
        Assert.assertEquals(USERNAME, studentAddResponse.getUsername());
        Assert.assertEquals(NAME, studentAddResponse.getName());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void addToCourse_Should_Throw_ObjectNotFoundException_If_Student_Is_Null() {
        StudentAddToCourseRequest studentAddToCourseRequest = new StudentAddToCourseRequest();
        studentAddToCourseRequest.setStudentId(ID);

        Mockito.when(mockStudentRepository.findById(ID))
                .thenReturn(Optional.empty());

        studentService.addToCourse(studentAddToCourseRequest);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void addToCourse_Should_Throw_ObjectNotFoundException_If_Course_Is_Null() {
        StudentAddToCourseRequest studentAddToCourseRequest = new StudentAddToCourseRequest();
        studentAddToCourseRequest.setStudentId(ID);
        studentAddToCourseRequest.setCourseName(COURSE_NAME);

        Mockito.when(mockStudentRepository.findById(ID))
                .thenReturn(Optional.of(new Student()));

        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
                .thenReturn(Optional.empty());

        studentService.addToCourse(studentAddToCourseRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addToCourse_Should_Throw_IllegalArgumentException_If_Student_Is_Enrolled() {
        StudentAddToCourseRequest studentAddToCourseRequest = new StudentAddToCourseRequest();
        studentAddToCourseRequest.setStudentId(ID);
        studentAddToCourseRequest.setCourseName(COURSE_NAME);

        Course course = new Course();
        course.setName(COURSE_NAME);

        Student student = new Student();
        student.getCourses().add(course);

        Mockito.when(mockStudentRepository.findById(ID))
                .thenReturn(Optional.of(student));

        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
                .thenReturn(Optional.of(course));

        studentService.addToCourse(studentAddToCourseRequest);
    }

    @Test
    public void addToCourse_Should_Work_Correctly() {
        StudentAddToCourseRequest studentAddToCourseRequest = new StudentAddToCourseRequest();
        studentAddToCourseRequest.setStudentId(ID);
        studentAddToCourseRequest.setCourseName(COURSE_NAME);

        Course course = new Course();
        course.setName(COURSE_NAME);

        Student student = new Student();
        student.setId(ID);

        Mockito.when(mockStudentRepository.findById(ID))
                .thenReturn(Optional.of(student));

        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
                .thenReturn(Optional.of(course));

        Mockito.when(mockStudentRepository.saveAndFlush(any(Student.class)))
                .thenReturn(student);

        StudentAddToCourseResponse studentAddToCourseResponse = studentService.addToCourse(studentAddToCourseRequest);

        Assert.assertEquals(ID, studentAddToCourseResponse.getStudentId());
        Assert.assertEquals(COURSE_NAME, studentAddToCourseResponse.getCourseName());
    }

    @Test
    public void getAllInCoursesAscByAverageGradeAsc_Should_Return_Correct_Result() {
        Grade excellent = new Grade();
        excellent.setValue(EXCELLENT_GRADE);

        Grade good = new Grade();
        good.setValue(GOOD_GRADE);

        Student student = new Student();
        student.setName(NAME);
        student.getGrades().add(excellent);
        student.getGrades().add(good);

        Course java = new Course();
        java.setName(COURSE_NAME);
        java.getStudents().add(student);
        excellent.setCourse(java);
        good.setCourse(java);

        Mockito.when(mockCourseRepository.findAllOrderedByNameAsc())
                .thenReturn(List.of(java));

        List<CourseAllOrderedResponse> allInCoursesAscByAverageGradeAsc = studentService.getAllInCoursesAscByAverageGradeAsc();
        Assert.assertFalse(allInCoursesAscByAverageGradeAsc.isEmpty());
        Assert.assertEquals(COURSE_NAME, allInCoursesAscByAverageGradeAsc.get(0).getName());
        Assert.assertEquals(NAME, allInCoursesAscByAverageGradeAsc.get(0).getStudents().get(0).getStudentName());
        Assert.assertEquals(Double.valueOf(5.00), allInCoursesAscByAverageGradeAsc.get(0).getStudents().get(0).getGradeValue());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getTotalAverageGrade_Should_Throw_IllegalArgumentException() {
        Mockito.when(mockStudentRepository.findById(ID))
                .thenReturn(Optional.empty());

        studentService.getTotalAverageGrade(ID.toString());
    }

    @Test
    public void getTotalAverageGrade_Should_Return_Correctly() {
        Grade excellent = new Grade();
        excellent.setValue(EXCELLENT_GRADE);

        Grade good = new Grade();
        good.setValue(GOOD_GRADE);

        Student student = new Student();
        student.setName(NAME);
        student.getGrades().add(excellent);
        student.getGrades().add(good);

        Course java = new Course();
        java.setName(COURSE_NAME);
        java.getStudents().add(student);
        excellent.setCourse(java);
        good.setCourse(java);
        student.getCourses().add(java);

        Mockito.when(mockStudentRepository.findById(ID))
                .thenReturn(Optional.of(student));

        Double totalAverageGrade = studentService.getTotalAverageGrade(ID.toString());
        Assert.assertEquals(Double.valueOf(5.00), totalAverageGrade);
    }

    @Test
    public void getTotalAverageGrade_Should_Return_Zero_If_No_Grades() {
        Student student = new Student();
        student.setName(NAME);

        Course java = new Course();
        java.setName(COURSE_NAME);
        java.getStudents().add(student);
        student.getCourses().add(java);

        Mockito.when(mockStudentRepository.findById(ID))
                .thenReturn(Optional.of(student));

        Double totalAverageGrade = studentService.getTotalAverageGrade(ID.toString());
        Assert.assertEquals(Double.valueOf(0.00), totalAverageGrade);
    }
}