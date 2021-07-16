package project.restapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.entities.Student;
import project.restapi.domain.models.api.request.StudentAddToCourseRequest;
import project.restapi.repository.CourseRepository;
import project.restapi.repository.GradeRepository;
import project.restapi.repository.StudentRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Test
    public void getTotalAverage_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/student/average/1")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"STUDENT"})
    public void getTotalAverage_Should_Return_NotFound_If_Student_Does_Not_Exist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/student/average/1")).
                andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"STUDENT"})
    public void getTotalAverage_Should_Return_Ok() throws Exception {
        courseRepository.deleteAll();

        Student student = new Student();
        student.setName("Name");
        student.setUsername("username");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909090000");
        student.setAge(22);
        studentRepository.saveAndFlush(student);

        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(90.0);
        courseRepository.saveAndFlush(course);

        Grade grade = new Grade();
        grade.setValue(6.0);
        grade.setStudent(student);
        grade.setCourse(course);
        gradeRepository.saveAndFlush(grade);

        assert studentRepository.findByUsername("username").isPresent();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/student/average/" + studentRepository.findByUsername("username").get().getId())).
                andExpect(status().isOk());
    }

    @Test
    public void addToCourse_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/student/add/course")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addToCourse_Should_Return_Not_Found_If_Student_Does_Not_Exist() throws Exception {
        StudentAddToCourseRequest studentAddToCourseRequest = new StudentAddToCourseRequest();
        studentAddToCourseRequest.setCourseName("Java");
        studentAddToCourseRequest.setStudentId(-1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddToCourseRequest);

        mockMvc.perform(post("/api/student/add/course").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addToCourse_Should_Return_Not_Found_If_Course_Does_Not_Exist() throws Exception {
        Student student = new Student();
        student.setName("Name");
        student.setUsername("username");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909090000");
        student.setAge(22);
        studentRepository.saveAndFlush(student);

        courseRepository.deleteAll();
        StudentAddToCourseRequest studentAddToCourseRequest = new StudentAddToCourseRequest();
        studentAddToCourseRequest.setCourseName("Java");
        assert studentRepository.findByUsername("username").isPresent();
        studentAddToCourseRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddToCourseRequest);

        mockMvc.perform(post("/api/student/add/course").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addToCourse_Should_Return_UnprocessableEntity_If_Student_Is_Already_Enrolled() throws Exception {
        Student student = new Student();
        student.setName("Name");
        student.setUsername("username");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909090000");
        student.setAge(22);
        studentRepository.saveAndFlush(student);

        courseRepository.deleteAll();
        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(90.0);
        courseRepository.saveAndFlush(course);

        student.getCourses().add(course);
        studentRepository.saveAndFlush(student);

        StudentAddToCourseRequest studentAddToCourseRequest = new StudentAddToCourseRequest();
        studentAddToCourseRequest.setCourseName("Java");
        assert studentRepository.findByUsername("username").isPresent();
        studentAddToCourseRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddToCourseRequest);

        mockMvc.perform(post("/api/student/add/course").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addToCourse_Should_Return_Ok() throws Exception {
        Student student = new Student();
        student.setName("Name");
        student.setUsername("username");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909090000");
        student.setAge(22);
        studentRepository.saveAndFlush(student);

        courseRepository.deleteAll();
        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(90.0);
        courseRepository.saveAndFlush(course);

        StudentAddToCourseRequest studentAddToCourseRequest = new StudentAddToCourseRequest();
        studentAddToCourseRequest.setCourseName("Java");
        assert studentRepository.findByUsername("username").isPresent();
        studentAddToCourseRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddToCourseRequest);

        mockMvc.perform(post("/api/student/add/course").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        gradeRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }
}