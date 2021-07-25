package project.restapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.restapi.constants.ErrorMessages;
import project.restapi.domain.entities.Administrator;
import project.restapi.domain.entities.Student;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.models.api.request.LoggedInUserRequest;
import project.restapi.domain.models.api.response.UserLoggedInResponse;
import project.restapi.repository.AdministratorRepository;
import project.restapi.repository.StudentRepository;
import project.restapi.repository.TeacherRepository;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdministratorRepository administratorRepository;

    @Autowired
    public UserDetailsServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository, AdministratorRepository administratorRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.administratorRepository = administratorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository
                .findByUsername(username)
                .orElse(null);

        Teacher teacher = teacherRepository
                .findByUsername(username)
                .orElse(null);

        Administrator administrator = administratorRepository
                .findByUsername(username)
                .orElse(null);

        if (student == null && teacher == null && administrator == null) {
            throw new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }

        return Objects.requireNonNullElseGet(student, () -> Objects.requireNonNullElse(teacher, administrator));
    }

    public UserLoggedInResponse getLoggedInUser(LoggedInUserRequest loggedInUserRequest) {
        return null;
    }
}
