package project.restapi.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.restapi.domain.entities.Role;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.entities.enums.Degree;
import project.restapi.domain.models.api.request.TeacherAddRequest;
import project.restapi.domain.models.api.response.TeacherAddResponse;
import project.restapi.exceptions.ObjectAlreadyExistsException;
import project.restapi.repository.RoleRepository;
import project.restapi.repository.TeacherRepository;
import project.restapi.service.TeacherService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class TeacherServiceTests {
    public static final Long ID = 1L;
    public static final String INVALID_NAME = "AA";
    public static final String NAME = "Name";
    public static final String INVALID_UCN = "990122";
    public static final String UCN = "9901220982";
    public static final String USERNAME = "john";
    public static final String BLANK_PASSWORD = "";
    public static final String PASSWORD = "password1234";
    public static final String ROLE_STUDENT = "ROLE_STUDENT";
    public static final String ROLE_TEACHER = "ROLE_TEAHER";
    public static final String DEGREE = "Bachelor";

    private TeacherRepository teacherRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;

    private TeacherService teacherService;

    @Before
    public void init() {
        roleRepository = Mockito.mock(RoleRepository.class);
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        teacherRepository = Mockito.mock(TeacherRepository.class);

        teacherService = new TeacherServiceImpl(teacherRepository, bCryptPasswordEncoder,
                new ModelMapper(), roleRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_Should_Throw_IllegalArgumentException_When_Name_Invalid() {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setName(INVALID_NAME);

        teacherService.add(teacherAddRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_Should_Throw_IllegalArgumentException_When_UCN_Invalid() {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setName(NAME);
        teacherAddRequest.setUniqueCitizenNumber(INVALID_UCN);

        teacherService.add(teacherAddRequest);
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void add_Should_Throw_ObjectAlreadyExistsException_When_Teacher_Exists_Based_On_UCN() {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setName(NAME);
        teacherAddRequest.setUniqueCitizenNumber(UCN);

        Mockito.when(teacherRepository.findByUniqueCitizenNumber(UCN))
                .thenReturn(Optional.of(new Teacher()));

        teacherService.add(teacherAddRequest);
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void add_Should_Throw_ObjectAlreadyExistsException_When_Teacher_Exists_Based_On_Username() {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setName(NAME);
        teacherAddRequest.setUniqueCitizenNumber(UCN);
        teacherAddRequest.setUsername(USERNAME);

        Mockito.when(teacherRepository.findByUniqueCitizenNumber(UCN))
                .thenReturn(Optional.empty());

        Mockito.when(teacherRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(new Teacher()));

        teacherService.add(teacherAddRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_Should_Throw_IllegalArgumentException_When_Password_Is_Blank() {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setName(NAME);
        teacherAddRequest.setUniqueCitizenNumber(UCN);
        teacherAddRequest.setUsername(USERNAME);
        teacherAddRequest.setPassword(BLANK_PASSWORD);

        Mockito.when(teacherRepository.findByUniqueCitizenNumber(UCN))
                .thenReturn(Optional.empty());

        Mockito.when(teacherRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        teacherService.add(teacherAddRequest);
    }

    @Test
    public void add_Should_Add_User_Successfully() {
        TeacherAddRequest teacherAddRequest = new TeacherAddRequest();
        teacherAddRequest.setName(NAME);
        teacherAddRequest.setUniqueCitizenNumber(UCN);
        teacherAddRequest.setUsername(USERNAME);
        teacherAddRequest.setPassword(PASSWORD);
        teacherAddRequest.setDegree(DEGREE);

        Mockito.when(teacherRepository.findByUniqueCitizenNumber(UCN))
                .thenReturn(Optional.empty());

        Mockito.when(teacherRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        Mockito.when(roleRepository.findByAuthority(ROLE_STUDENT))
                .thenReturn(new Role(ROLE_STUDENT));

        Mockito.when(roleRepository.findByAuthority(ROLE_TEACHER))
                .thenReturn(new Role(ROLE_TEACHER));

        Mockito.when(bCryptPasswordEncoder.encode(PASSWORD))
                .thenReturn(PASSWORD);

        Teacher teacher = new Teacher();
        teacher.setId(ID);
        teacher.setUsername(USERNAME);
        teacher.setName(NAME);
        teacher.setDegree(Degree.BACHELOR);

        Mockito.when(teacherRepository.saveAndFlush(any(Teacher.class)))
                .thenReturn(teacher);

        TeacherAddResponse teacherAddResponse = teacherService.add(teacherAddRequest);

        Assert.assertEquals(ID, teacherAddResponse.getId());
        Assert.assertEquals(USERNAME, teacherAddResponse.getUsername());
        Assert.assertEquals(NAME, teacherAddResponse.getName());
        Assert.assertEquals(Degree.BACHELOR, teacherAddResponse.getDegree());
    }
}
