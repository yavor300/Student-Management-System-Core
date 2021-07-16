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
import project.restapi.domain.entities.Student;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.entities.enums.Degree;
import project.restapi.domain.models.api.request.ChangeRoleStudentRequest;
import project.restapi.domain.models.api.request.ChangeRoleTeacherRequest;
import project.restapi.repository.RoleRepository;
import project.restapi.repository.StudentRepository;
import project.restapi.repository.TeacherRepository;
import project.restapi.service.RoleService;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class RoleRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void makeTeacherAdmin_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/role/teacher/admin")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeTeacherAdmin_Should_Return_NotFound_If_Teacher_Does_Not_Exist() throws Exception {
        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleTeacherRequest);

        mockMvc.perform(post("/api/role/teacher/admin").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeTeacherAdmin_Should_Return_UnprocessableEntity_If_Auth_Size_Is_Less_Than_2() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setDegree(Degree.BACHELOR);
        teacher.setUsername("username");
        teacher.setUniqueCitizenNumber("9909091234");
        teacher.setPassword(bCryptPasswordEncoder.encode("password"));
        teacher.setName("Name");
        teacher.setAuthorities(new LinkedHashSet<>());
        teacherRepository.saveAndFlush(teacher);

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        assert teacherRepository.findByUsername("username").isPresent();
        changeRoleTeacherRequest.setTeacherId(teacherRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleTeacherRequest);

        mockMvc.perform(post("/api/role/admin/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeTeacherAdmin_Should_Return_UnprocessableEntity_If_Auth_Size_Is_Three() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setDegree(Degree.BACHELOR);
        teacher.setUsername("username");
        teacher.setUniqueCitizenNumber("9909091234");
        teacher.setPassword(bCryptPasswordEncoder.encode("password"));
        teacher.setName("Name");
        teacher.setAuthorities(roleService.getRolesForAdmin());
        teacherRepository.saveAndFlush(teacher);

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        assert teacherRepository.findByUsername("username").isPresent();
        changeRoleTeacherRequest.setTeacherId(teacherRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleTeacherRequest);

        mockMvc.perform(post("/api/role/admin/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeTeacherAdmin_Should_Return_Ok() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setDegree(Degree.BACHELOR);
        teacher.setUsername("username");
        teacher.setUniqueCitizenNumber("9909091234");
        teacher.setPassword(bCryptPasswordEncoder.encode("password"));
        teacher.setName("Name");
        teacher.setAuthorities(Set.of(roleRepository.findByAuthority("ROLE_STUDENT"),
                roleRepository.findByAuthority("ROLE_TEACHER")));
        teacherRepository.saveAndFlush(teacher);

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        assert teacherRepository.findByUsername("username").isPresent();
        changeRoleTeacherRequest.setTeacherId(teacherRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleTeacherRequest);

        mockMvc.perform(post("/api/role/admin/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void restoreTeacher_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/role/restore/teacher")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void restoreTeacher_Should_Return_Not_Found_If_Teacher_Does_Not_Exist() throws Exception {
        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        changeRoleTeacherRequest.setTeacherId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleTeacherRequest);

        mockMvc.perform(post("/api/role/restore/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void restoreTeacher_Should_Return_UnprocessableEntity_If_Auth_Size_Is_Less_Than_3() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setDegree(Degree.BACHELOR);
        teacher.setUsername("username");
        teacher.setUniqueCitizenNumber("9909091234");
        teacher.setPassword(bCryptPasswordEncoder.encode("password"));
        teacher.setName("Name");
        teacher.setAuthorities(new LinkedHashSet<>());
        teacherRepository.saveAndFlush(teacher);

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        assert teacherRepository.findByUsername("username").isPresent();
        changeRoleTeacherRequest.setTeacherId(teacherRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleTeacherRequest);

        mockMvc.perform(post("/api/role/restore/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void restoreTeacher_Should_Return_Ok() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setDegree(Degree.BACHELOR);
        teacher.setUsername("username");
        teacher.setUniqueCitizenNumber("9909091234");
        teacher.setPassword(bCryptPasswordEncoder.encode("password"));
        teacher.setName("Name");
        teacher.setAuthorities(roleService.getRolesForAdmin());
        teacherRepository.saveAndFlush(teacher);

        ChangeRoleTeacherRequest changeRoleTeacherRequest = new ChangeRoleTeacherRequest();
        assert teacherRepository.findByUsername("username").isPresent();
        changeRoleTeacherRequest.setTeacherId(teacherRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleTeacherRequest);

        mockMvc.perform(post("/api/role/restore/teacher").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void makeStudentTeacher_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/role/teacher/student")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeStudentTeacher_Should_Return_Not_Found_If_Student_Does_Not_Exist() throws Exception {
        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/teacher/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeStudentTeacher_Should_Return_UnprocessableEntity_If_Auth_Size_Is_Two() throws Exception {
        Student student = new Student();
        student.setUsername("username");
        student.setAge(20);
        student.setUniqueCitizenNumber("9909090000");
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setAuthorities(Set.of(
                roleRepository.findByAuthority("ROLE_STUDENT"),
                roleRepository.findByAuthority("ROLE_TEACHER")
        ));
        studentRepository.saveAndFlush(student);

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        assert studentRepository.findByUsername("username").isPresent();
        changeRoleStudentRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/teacher/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeStudentTeacher_Should_Return_Ok() throws Exception {
        Student student = new Student();
        student.setUsername("username");
        student.setAge(20);
        student.setUniqueCitizenNumber("9909090000");
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setAuthorities(Set.of(
                roleRepository.findByAuthority("ROLE_STUDENT")
        ));
        studentRepository.saveAndFlush(student);

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        assert studentRepository.findByUsername("username").isPresent();
        changeRoleStudentRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/teacher/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void makeStudentAdmin_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/role/admin/student")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeStudentAdmin_Should_Return_Not_Found_If_Student_Does_Not_Exist() throws Exception {
        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/admin/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeStudentAdmin_Should_Return_UnprocessableEntity_If_Auth_Size_Is_Three() throws Exception {
        Student student = new Student();
        student.setUsername("username");
        student.setAge(20);
        student.setUniqueCitizenNumber("9909090000");
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setAuthorities(roleService.getRolesForAdmin());
        studentRepository.saveAndFlush(student);

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        assert studentRepository.findByUsername("username").isPresent();
        changeRoleStudentRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/admin/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void makeStudentAdmin_Should_Return_Ok() throws Exception {
        Student student = new Student();
        student.setUsername("username");
        student.setAge(20);
        student.setUniqueCitizenNumber("9909090000");
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setAuthorities(Set.of(
                roleRepository.findByAuthority("ROLE_STUDENT")
        ));
        studentRepository.saveAndFlush(student);

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        assert studentRepository.findByUsername("username").isPresent();
        changeRoleStudentRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/admin/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void restoreStudent_Should_Return_Forbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/role/restore/student")).
                andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void restoreStudent_Should_Return_Not_Found_If_Student_Does_Not_Exist() throws Exception {
        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        changeRoleStudentRequest.setStudentId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/restore/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void restoreStudent_Should_Return_UnprocessableEntity_If_Auth_Size_Is_One() throws Exception {
        Student student = new Student();
        student.setUsername("username");
        student.setAge(20);
        student.setUniqueCitizenNumber("9909090000");
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setAuthorities(Set.of(
                roleRepository.findByAuthority("ROLE_STUDENT")
        ));
        studentRepository.saveAndFlush(student);

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();
        assert studentRepository.findByUsername("username").isPresent();
        changeRoleStudentRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/restore/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void restoreStudent_Should_Return_Ok() throws Exception {
        Student student = new Student();
        student.setUsername("username");
        student.setAge(20);
        student.setUniqueCitizenNumber("9909090000");
        student.setName("Name");
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setAuthorities(Set.of(
                roleRepository.findByAuthority("ROLE_STUDENT"),
                roleRepository.findByAuthority("ROLE_TEACHER")
        ));
        studentRepository.saveAndFlush(student);

        ChangeRoleStudentRequest changeRoleStudentRequest = new ChangeRoleStudentRequest();

        assert studentRepository.findByUsername("username").isPresent();
        changeRoleStudentRequest.setStudentId(studentRepository.findByUsername("username").get().getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(changeRoleStudentRequest);

        mockMvc.perform(post("/api/role/restore/student").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
    }
}
