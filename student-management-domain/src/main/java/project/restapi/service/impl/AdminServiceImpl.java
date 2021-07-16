package project.restapi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.restapi.constants.AdminCredentials;
import project.restapi.constants.ErrorMessages;
import project.restapi.constants.RoleValues;
import project.restapi.domain.entities.*;
import project.restapi.domain.models.api.request.ChangeRoleStudentRequest;
import project.restapi.domain.models.api.request.ChangeRoleTeacherRequest;
import project.restapi.domain.models.api.response.ChangeRoleStudentResponse;
import project.restapi.domain.models.api.response.ChangeRoleTeacherResponse;
import project.restapi.domain.models.api.response.CourseAllWithTeacherAndAverageResponse;
import project.restapi.exceptions.ObjectNotFoundException;
import project.restapi.repository.*;
import project.restapi.service.AdminService;
import project.restapi.service.RoleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdministratorRepository administratorRepository;
    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AdminServiceImpl(AdministratorRepository administratorRepository, TeacherRepository teacherRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService, ModelMapper modelMapper, StudentRepository studentRepository,
                            CourseRepository courseRepository) {
        this.administratorRepository = administratorRepository;
        this.teacherRepository = teacherRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void seedAdmin() {
        if (administratorRepository.count() == 0) {
            Administrator administrator = new Administrator();
            administrator.setName(AdminCredentials.ADMIN_NAME);
            administrator.setUsername(AdminCredentials.ADMIN_USERNAME);
            administrator.setPassword(bCryptPasswordEncoder.encode(AdminCredentials.ADMIN_PASSWORD));
            administrator.setAuthorities(roleService.getRolesForAdmin());
            administratorRepository.saveAndFlush(administrator);
        }
    }

    @Override
    public ChangeRoleTeacherResponse makeTeacherAdmin(ChangeRoleTeacherRequest changeRoleTeacherRequest) {
        Teacher teacher = teacherRepository.findById(changeRoleTeacherRequest.getTeacherId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.TEACHER_NOT_FOUND));

        if (teacher.getAuthorities().size() < 2) {
            throw new IllegalArgumentException(ErrorMessages.USER_IS_NOT_A_TEACHER);
        }

        if (teacher.getAuthorities().size() == 3) {
            throw new IllegalArgumentException(ErrorMessages.USER_IS_ALREADY_ADMIN);
        }

        teacher.getAuthorities().clear();
        teacher.setAuthorities(roleService.getRolesForAdmin());

        ChangeRoleTeacherResponse changeRoleTeacherResponse = modelMapper.map(teacherRepository.saveAndFlush(teacher), ChangeRoleTeacherResponse.class);
        changeRoleTeacherResponse.setTeacherId(teacher.getId());

        return changeRoleTeacherResponse;
    }

    @Override
    public ChangeRoleTeacherResponse restoreTeacher(ChangeRoleTeacherRequest changeRoleTeacherRequest) {
        Teacher teacher = teacherRepository.findById(changeRoleTeacherRequest.getTeacherId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.TEACHER_NOT_FOUND));

        if (teacher.getAuthorities().size() < 3) {
            throw new IllegalArgumentException(ErrorMessages.USER_IS_NOT_AN_ADMIN);
        }

        teacher.getAuthorities().clear();
        teacher.getAuthorities().add(roleRepository.findByAuthority(RoleValues.STUDENT));
        teacher.getAuthorities().add(roleRepository.findByAuthority(RoleValues.TEACHER));

        ChangeRoleTeacherResponse changeRoleTeacherResponse = modelMapper.map(teacherRepository.saveAndFlush(teacher), ChangeRoleTeacherResponse.class);
        changeRoleTeacherResponse.setTeacherId(teacher.getId());

        return changeRoleTeacherResponse;
    }

    @Override
    public ChangeRoleStudentResponse makeStudentTeacher(ChangeRoleStudentRequest changeRoleStudentRequest) {
        Student student = studentRepository.findById(changeRoleStudentRequest.getStudentId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        if (student.getAuthorities().size() == 2) {
            throw new IllegalArgumentException(ErrorMessages.USER_IS_ALREADY_TEACHER);
        }

        student.getAuthorities().clear();
        student.getAuthorities().add(roleRepository.findByAuthority(RoleValues.STUDENT));
        student.getAuthorities().add(roleRepository.findByAuthority(RoleValues.TEACHER));

        ChangeRoleStudentResponse changeRoleStudentResponse = modelMapper.map(studentRepository.saveAndFlush(student), ChangeRoleStudentResponse.class);
        changeRoleStudentResponse.setStudentId(student.getId());

        return changeRoleStudentResponse;
    }

    @Override
    public ChangeRoleStudentResponse makeStudentAdmin(ChangeRoleStudentRequest changeRoleStudentRequest) {
        Student student = studentRepository.findById(changeRoleStudentRequest.getStudentId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        if (student.getAuthorities().size() == 3) {
            throw new IllegalArgumentException(ErrorMessages.USER_IS_ALREADY_ADMIN);
        }

        student.getAuthorities().clear();
        student.setAuthorities(roleService.getRolesForAdmin());

        ChangeRoleStudentResponse changeRoleStudentResponse = modelMapper.map(studentRepository.saveAndFlush(student), ChangeRoleStudentResponse.class);
        changeRoleStudentResponse.setStudentId(student.getId());

        return changeRoleStudentResponse;
    }

    @Override
    public ChangeRoleStudentResponse restoreStudent(ChangeRoleStudentRequest changeRoleStudentRequest) {
        Student student = studentRepository.findById(changeRoleStudentRequest.getStudentId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        if (student.getAuthorities().size() == 1) {
            throw new IllegalArgumentException(ErrorMessages.USER_IS_NOT_AN_ADMIN_OR_A_TEACHER);
        }

        student.getAuthorities().clear();
        student.getAuthorities().add(roleRepository.findByAuthority(RoleValues.STUDENT));

        ChangeRoleStudentResponse changeRoleStudentResponse = modelMapper.map(studentRepository.saveAndFlush(student), ChangeRoleStudentResponse.class);
        changeRoleStudentResponse.setStudentId(student.getId());

        return changeRoleStudentResponse;
    }

    @Override
    public List<CourseAllWithTeacherAndAverageResponse> getAllCoursesWithTeachersAndAverage() {
        List<CourseAllWithTeacherAndAverageResponse> result = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            CourseAllWithTeacherAndAverageResponse response = modelMapper.map(course, CourseAllWithTeacherAndAverageResponse.class);
            double average = course.getGrades()
                    .stream()
                    .mapToDouble(Grade::getValue)
                    .average()
                    .orElse(0.0);
            response.setAverageGrade(average);

            if (course.getTeacher() != null) {
                response.setTeacherName(course.getTeacher().getName());
            } else {
                response.setTeacherName(null);
            }
            result.add(response);
        }
        return result;
    }
}
