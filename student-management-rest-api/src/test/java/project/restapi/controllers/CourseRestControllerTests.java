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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.entities.Student;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.entities.enums.Degree;
import project.restapi.domain.models.api.request.CourseAddRequest;
import project.restapi.domain.models.api.request.CourseAddTeacherRequest;
import project.restapi.repository.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    StudentRepository studentRepository;

    @Test
    @WithMockUser(roles = {"STUDENT"})
    public void get_All_Courses_Should_Return_Correct_Result() throws Exception {
        courseRepository.deleteAll();
        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(100.0);
        courseRepository.saveAndFlush(course);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/course/all")).
                andExpect(status().isOk()).
                andExpect(jsonPath("[0].name").value("Java")).
                andExpect(jsonPath("[0].totalHours").value(100.0));
    }

    @Test
    public void getAllCourses_Should_Return_403_Forbidden() throws Exception {
        courseRepository.deleteAll();
        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(100.0);
        courseRepository.saveAndFlush(course);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/course/all")).
                andExpect(status().isForbidden());
    }

    @Test
    public void getAllCoursesOrdered_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/course/all/ordered")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    public void getAllInCoursesOrdered_Should_Return_Correct_Result() throws Exception {
        courseRepository.deleteAll();
        Course java = new Course();
        java.setName("Java");
        java.setTotalHours(90.0);
        Course c = new Course();
        c.setName("C");
        c.setTotalHours(240.0);
        courseRepository.saveAndFlush(java);
        courseRepository.saveAndFlush(c);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/course/all/ordered")).
                andExpect(status().isOk())
                .andExpect(jsonPath("[0].name").value("C"));
    }

    @Test
    public void getAverageInCourse_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/course/average/Java")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"STUDENT"})
    public void getAverageInCourse_Should_Return_Not_Found() throws Exception {
        courseRepository.deleteAll();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/course/average/Java")).
                andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"STUDENT"})
    public void getAverageInCourse_Should_Return_Correct_Result() throws Exception {
        courseRepository.deleteAll();
        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(800.0);
        courseRepository.saveAndFlush(course);

        Student student = new Student();
        student.setName("John");
        student.setPassword("password");
        student.setUsername("john");
        student.setUniqueCitizenNumber("9901011234");
        student.setAge(22);
        student.getCourses().add(course);
        studentRepository.saveAndFlush(student);

        Grade grade = new Grade();
        grade.setValue(6.0);
        grade.setCourse(course);
        grade.setStudent(student);
        gradeRepository.saveAndFlush(grade);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/course/average/Java")).
                andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Java"))
                .andExpect(jsonPath("students[0].studentName").value("John"))
                .andExpect(jsonPath("students[0].gradeValue").value("6.0"));
    }

    @Test
    public void addCourse_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                post("/api/course/add")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void add_Course_Should_Return_Conflict() throws Exception {
        courseRepository.deleteAll();
        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(800.0);
        courseRepository.saveAndFlush(course);

        CourseAddRequest courseAddRequest = new CourseAddRequest();
        courseAddRequest.setName("Java");
        courseAddRequest.setTotalHours(800.0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(courseAddRequest);

        mockMvc.perform(post("/api/course/add").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void add_Course_Should_Add_Course_Successfully() throws Exception {
        courseRepository.deleteAll();

        CourseAddRequest courseAddRequest = new CourseAddRequest();
        courseAddRequest.setName("Java");
        courseAddRequest.setTotalHours(800.0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(courseAddRequest);

        mockMvc.perform(post("/api/course/add").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void addTeacher_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                post("/api/course/add/teacher")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void addTeacher_Should_Return_NotFound() throws Exception {
        courseRepository.deleteAll();

        CourseAddTeacherRequest courseAddTeacherRequest = new CourseAddTeacherRequest();
        courseAddTeacherRequest.setTeacherId(1L);
        courseAddTeacherRequest.setCourseName("C");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(courseAddTeacherRequest);

        mockMvc.perform(post("/api/course/add/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void addTeacher_Should_Return_UnprocessableEntity() throws Exception {
        courseRepository.deleteAll();

        Teacher teacher = new Teacher();
        teacher.setName("Alex");
        teacher.setDegree(Degree.BACHELOR);
        teacher.setPassword("password");
        teacher.setUniqueCitizenNumber("9901014567");
        teacher.setUsername("alex");
        teacherRepository.saveAndFlush(teacher);

        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(800.0);
        course.setTeacher(teacher);
        courseRepository.saveAndFlush(course);

        CourseAddTeacherRequest courseAddTeacherRequest = new CourseAddTeacherRequest();
        courseAddTeacherRequest.setTeacherId(teacherRepository.findByUsername("alex").get().getId());
        courseAddTeacherRequest.setCourseName("Java");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(courseAddTeacherRequest);

        mockMvc.perform(post("/api/course/add/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void addTeacher_Should_Add_Successfully() throws Exception {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();

        Teacher teacher = new Teacher();
        teacher.setName("Alex");
        teacher.setDegree(Degree.BACHELOR);
        teacher.setPassword("password");
        teacher.setUniqueCitizenNumber("9901014567");
        teacher.setUsername("alex");
        teacherRepository.saveAndFlush(teacher);

        Course course = new Course();
        course.setName("Java");
        course.setTotalHours(800.0);
        courseRepository.saveAndFlush(course);

        CourseAddTeacherRequest courseAddTeacherRequest = new CourseAddTeacherRequest();
        courseAddTeacherRequest.setTeacherId(teacherRepository.findByUsername("alex").get().getId());
        courseAddTeacherRequest.setCourseName("Java");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(courseAddTeacherRequest);

        mockMvc.perform(post("/api/course/add/teacher").contentType(APPLICATION_JSON)
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