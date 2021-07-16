package project.restapi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.restapi.constants.ErrorMessages;
import project.restapi.constants.RegexValidations;
import project.restapi.constants.RoleValues;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.entities.enums.Degree;
import project.restapi.domain.models.api.request.TeacherAddRequest;
import project.restapi.domain.models.api.response.CourseAverageAll;
import project.restapi.domain.models.api.response.TeacherAddResponse;
import project.restapi.exceptions.ObjectAlreadyExistsException;
import project.restapi.repository.RoleRepository;
import project.restapi.repository.TeacherRepository;
import project.restapi.service.TeacherService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, RoleRepository roleRepository) {
        this.teacherRepository = teacherRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public TeacherAddResponse add(TeacherAddRequest teacherAddRequest) {
        if (!teacherAddRequest.getName().matches(RegexValidations.FIRST_NAME_VALIDATOR)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_NAME);
        }

        if (!teacherAddRequest.getUniqueCitizenNumber().matches(RegexValidations.UNIQUE_CITIZEN_NUMBER_VALIDATOR)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_UCN);
        }

        if (teacherRepository.findByUniqueCitizenNumber(teacherAddRequest.getUniqueCitizenNumber()).isPresent() ||
                teacherRepository.findByUsername(teacherAddRequest.getUsername()).isPresent()) {
            throw new ObjectAlreadyExistsException(ErrorMessages.TEACHER_EXISTS);
        }

        if (teacherAddRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_PASSWORD);
        }

        Teacher teacher = new Teacher();
        modelMapper.map(teacherAddRequest, teacher);
        teacher.setDegree(Degree.valueOf(teacherAddRequest.getDegree().toUpperCase()));
        teacher.getAuthorities().add(roleRepository.findByAuthority(RoleValues.TEACHER));
        teacher.getAuthorities().add(roleRepository.findByAuthority(RoleValues.STUDENT));
        teacher.setPassword(bCryptPasswordEncoder.encode(teacherAddRequest.getPassword()));

        return modelMapper.map(teacherRepository.saveAndFlush(teacher), TeacherAddResponse.class);
    }

    @Override
    public List<CourseAverageAll> getCoursesAllAverage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Teacher teacher = (Teacher) authentication.getPrincipal();
        List<CourseAverageAll> result = new ArrayList<>();

        assert teacherRepository.findByUsername(teacher.getUsername()).isPresent();

        for (Course course : teacherRepository.findByUsername(teacher.getUsername()).get().getCourses()) {
            CourseAverageAll courseAverageAll = modelMapper.map(course, CourseAverageAll.class);
            double average = course.getGrades()
                    .stream()
                    .mapToDouble(Grade::getValue)
                    .average()
                    .orElse(0.0);
            courseAverageAll.setAverageGrade(average);
            result.add(courseAverageAll);
        }

        return result;
    }
}