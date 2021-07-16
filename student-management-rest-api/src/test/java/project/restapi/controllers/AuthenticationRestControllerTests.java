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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import project.restapi.domain.entities.Student;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.entities.enums.Degree;
import project.restapi.domain.models.api.request.StudentAddRequest;
import project.restapi.domain.models.api.request.TeacherAddRequest;
import project.restapi.domain.models.api.request.UserLoginRequest;
import project.restapi.repository.StudentRepository;
import project.restapi.repository.TeacherRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthenticationRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void login_Should_Return_Unauthorized_If_Credentials_Are_Wrong() throws Exception {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("Non-Existent");
        userLoginRequest.setPassword("wrong");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userLoginRequest);

        mockMvc.perform(post("/api/public/login").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void login_Should_Login_User_Successfully() throws Exception {
        Student student = new Student();
        student.setUsername("username");
        student.setAge(19);
        student.setUniqueCitizenNumber("9909091234");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setName("Name");
        studentRepository.saveAndFlush(student);

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username");
        userLoginRequest.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userLoginRequest);

        mockMvc.perform(post("/api/public/login").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void register_Should_Return_UnprocessableEntity_If_Student_Name_Is_Invalid() throws Exception {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setUsername("yavor");
        studentAddRequest.setPassword("password");
        studentAddRequest.setUniqueCitizenNumber("9909091234");
        studentAddRequest.setName("YAvor");
        studentAddRequest.setAge(22);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddRequest);

        mockMvc.perform(post("/api/public/register/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    public void register_Should_Return_UnprocessableEntity_If_Student_UCN_Is_Invalid() throws Exception {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setUsername("yavor");
        studentAddRequest.setPassword("password");
        studentAddRequest.setUniqueCitizenNumber("99090912345");
        studentAddRequest.setName("Yavor");
        studentAddRequest.setAge(22);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddRequest);

        mockMvc.perform(post("/api/public/register/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void register_Should_Return_Conflict_If_Student_Exists_By_UCN() throws Exception {
        Student student = new Student();
        student.setUsername("yavor");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909091234");
        student.setName("Yavor");
        student.setAge(22);
        studentRepository.saveAndFlush(student);

        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setUsername("yavor2");
        studentAddRequest.setPassword("password");
        studentAddRequest.setUniqueCitizenNumber("9909091234");
        studentAddRequest.setName("Yavor");
        studentAddRequest.setAge(22);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddRequest);

        mockMvc.perform(post("/api/public/register/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    public void register_Should_Return_Conflict_If_Student_Exists_By_Username() throws Exception {
        Student student = new Student();
        student.setUsername("yavor");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setUniqueCitizenNumber("9909091234");
        student.setName("Yavor");
        student.setAge(22);
        studentRepository.saveAndFlush(student);

        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setUsername("yavor");
        studentAddRequest.setPassword("password");
        studentAddRequest.setUniqueCitizenNumber("9909091233");
        studentAddRequest.setName("Yavor");
        studentAddRequest.setAge(22);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddRequest);

        mockMvc.perform(post("/api/public/register/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    public void register_Should_Return_UnprocessableEntity_If_Student_Password_Is_Blank() throws Exception {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setUsername("yavor");
        studentAddRequest.setPassword("");
        studentAddRequest.setUniqueCitizenNumber("9909091233");
        studentAddRequest.setName("Yavor");
        studentAddRequest.setAge(22);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddRequest);

        mockMvc.perform(post("/api/public/register/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void register_Should_Register_Student_Successfully() throws Exception {
        StudentAddRequest studentAddRequest = new StudentAddRequest();
        studentAddRequest.setUsername("yavor");
        studentAddRequest.setPassword("password");
        studentAddRequest.setUniqueCitizenNumber("9909091233");
        studentAddRequest.setName("Yavor");
        studentAddRequest.setAge(22);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(studentAddRequest);

        mockMvc.perform(post("/api/public/register/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void register_Should_Return_UnprocessableEntity_If_Teacher_Name_Is_Invalid() throws Exception {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setDegree("bachelor");
        teacherAddRequest.setName("yAVOR");
        teacherAddRequest.setUsername("yavor");
        teacherAddRequest.setPassword("password");
        teacherAddRequest.setUniqueCitizenNumber("9909091234");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(teacherAddRequest);

        mockMvc.perform(post("/api/public/register/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void register_Should_Return_UnprocessableEntity_If_Teacher_UCN_Is_Invalid() throws Exception {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setDegree("bachelor");
        teacherAddRequest.setName("Yavor");
        teacherAddRequest.setUsername("yavor");
        teacherAddRequest.setPassword("password");
        teacherAddRequest.setUniqueCitizenNumber("99090912345");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(teacherAddRequest);

        mockMvc.perform(post("/api/public/register/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void register_Should_Return_Conflict_If_Teacher_Exists_By_UCN() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setUsername("yavor");
        teacher.setPassword(bCryptPasswordEncoder.encode("password"));
        teacher.setUniqueCitizenNumber("9909091234");
        teacher.setName("Yavor");
        teacher.setDegree(Degree.BACHELOR);
        teacherRepository.saveAndFlush(teacher);

        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setDegree("bachelor");
        teacherAddRequest.setName("Yavor");
        teacherAddRequest.setUsername("yavor2");
        teacherAddRequest.setPassword("password");
        teacherAddRequest.setUniqueCitizenNumber("9909091234");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(teacherAddRequest);

        mockMvc.perform(post("/api/public/register/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    public void register_Should_Return_Conflict_If_Teacher_Exists_By_Username() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setUsername("yavor");
        teacher.setPassword(bCryptPasswordEncoder.encode("password"));
        teacher.setUniqueCitizenNumber("9909091234");
        teacher.setName("Yavor");
        teacher.setDegree(Degree.BACHELOR);
        teacherRepository.saveAndFlush(teacher);

        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setDegree("bachelor");
        teacherAddRequest.setName("Yavor");
        teacherAddRequest.setUsername("yavor");
        teacherAddRequest.setPassword("password");
        teacherAddRequest.setUniqueCitizenNumber("9909091233");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(teacherAddRequest);

        mockMvc.perform(post("/api/public/register/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict());
    }


    @Test
    public void register_Should_Return_UnprocessableEntity_If_Teacher_Password_Is_Blank() throws Exception {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setDegree("bachelor");
        teacherAddRequest.setName("Yavor");
        teacherAddRequest.setUsername("yavor");
        teacherAddRequest.setPassword("");
        teacherAddRequest.setUniqueCitizenNumber("9909091233");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(teacherAddRequest);

        mockMvc.perform(post("/api/public/register/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void register_Should_Register_Teacher_Successfully() throws Exception {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setDegree("bachelor");
        teacherAddRequest.setName("Yavor");
        teacherAddRequest.setUsername("yavor");
        teacherAddRequest.setPassword("password");
        teacherAddRequest.setUniqueCitizenNumber("9909091233");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(teacherAddRequest);

        mockMvc.perform(post("/api/public/register/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

        @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
    }
}