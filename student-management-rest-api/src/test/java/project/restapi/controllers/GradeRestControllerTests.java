package project.restapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import project.restapi.domain.entities.Student;
import project.restapi.domain.models.api.request.GradeAddRequest;
import project.restapi.repository.CourseRepository;
import project.restapi.repository.GradeRepository;
import project.restapi.repository.StudentRepository;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class GradeRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Test
    public void addGrade_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/grade/add")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addGrade_Should_Return_UnprocessableEntity_If_Grade_Is_Invalid_Lower() throws Exception {
        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(1.0);
        gradeAddRequest.setCourseName("Java");
        gradeAddRequest.setStudentId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(gradeAddRequest);

        mockMvc.perform(post("/api/grade/add").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addGrade_Should_Return_UnprocessableEntity_If_Grade_Is_Invalid_Higher() throws Exception {
        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(7.0);
        gradeAddRequest.setCourseName("Java");
        gradeAddRequest.setStudentId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(gradeAddRequest);

        mockMvc.perform(post("/api/grade/add").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addGrade_Should_Return_NotFound_If_Student_Does_Not_Exist() throws Exception {
        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(6.0);
        gradeAddRequest.setCourseName("Java");
        gradeAddRequest.setStudentId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(gradeAddRequest);

        mockMvc.perform(post("/api/grade/add").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addGrade_Should_Return_NotFound_If_Course_Does_Not_Exist() throws Exception {
        Student student = new Student();
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909091234");
        student.setUsername("username");
        student.setAge(22);
        studentRepository.saveAndFlush(student);

        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(6.0);
        gradeAddRequest.setStudentId(1L);
        gradeAddRequest.setCourseName("Non-Existent");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(gradeAddRequest);

        mockMvc.perform(post("/api/grade/add").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addGrade_Should_Return_UnprocessableEntity_If_Student_Is_Not_Signed_In_Course() throws Exception {
        courseRepository.deleteAll();

        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(90.0);
        courseRepository.saveAndFlush(course);

        Student student = new Student();
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909091234");
        student.setUsername("username");
        student.setAge(22);
        studentRepository.saveAndFlush(student);

        List<Student> all = studentRepository.findAll();

        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(5.0);
        gradeAddRequest.setCourseName("Java");
        gradeAddRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(gradeAddRequest);

        mockMvc.perform(post("/api/grade/add").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void addGrade_Should_Return_Ok() throws Exception {
        courseRepository.deleteAll();
        studentRepository.deleteAll();

        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(90.0);
        courseRepository.saveAndFlush(course);

        Student student = new Student();
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909091234");
        student.setUsername("username");
        student.setAge(22);
        student.getCourses().add(course);
        studentRepository.saveAndFlush(student);

        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(5.0);
        gradeAddRequest.setCourseName("Java");
        gradeAddRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(gradeAddRequest);

        mockMvc.perform(post("/api/grade/add").contentType(APPLICATION_JSON)
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
