package project.restapi.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.restapi.domain.entities.Administrator;
import project.restapi.domain.entities.Student;
import project.restapi.domain.entities.Teacher;
import project.restapi.repository.AdministratorRepository;
import project.restapi.repository.StudentRepository;
import project.restapi.repository.TeacherRepository;

import java.util.Optional;

public class UserDetailsTests {
    public static final String USERNAME = "johndoe";

    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;
    private AdministratorRepository administratorRepository;
    private UserDetailsService userDetailsService;

    @Before
    public void init() {
        teacherRepository = Mockito.mock(TeacherRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        administratorRepository = Mockito.mock(AdministratorRepository.class);

        userDetailsService = new UserDetailsServiceImpl(studentRepository, teacherRepository, administratorRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_Should_Throw_UsernameNotFoundException() {
        Mockito.when(studentRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        Mockito.when(teacherRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        Mockito.when(administratorRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        userDetailsService.loadUserByUsername(USERNAME);
    }

    @Test
    public void loadUserByUsername_Should_Return_Student() {
        Mockito.when(studentRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(new Student()));

        Mockito.when(teacherRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        Mockito.when(administratorRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);
        Assert.assertNotNull(userDetails);
    }

    @Test
    public void loadUserByUsername_Should_Return_Teacher() {
        Mockito.when(studentRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        Mockito.when(teacherRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(new Teacher()));

        Mockito.when(administratorRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);
        Assert.assertNotNull(userDetails);
    }

    @Test
    public void loadUserByUsername_Should_Return_Admin() {
        Mockito.when(studentRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        Mockito.when(teacherRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        Mockito.when(administratorRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(new Administrator()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);
        Assert.assertNotNull(userDetails);
    }
}
