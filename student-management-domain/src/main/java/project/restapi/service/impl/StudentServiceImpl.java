package project.restapi.service.impl;

import project.restapi.constants.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.entities.Student;
import project.restapi.domain.models.api.mapping.CourseAndStudentsOrderedMapper;
import project.restapi.domain.models.api.request.CourseAvailableStudentsRequest;
import project.restapi.domain.models.api.request.StudentAddRequest;
import project.restapi.domain.models.api.request.StudentAddToCourseRequest;
import project.restapi.domain.models.api.response.*;
import project.restapi.exceptions.ObjectAlreadyExistsException;
import project.restapi.exceptions.ObjectNotFoundException;
import project.restapi.repository.CourseRepository;
import project.restapi.repository.RoleRepository;
import project.restapi.repository.StudentRepository;
import project.restapi.service.StudentService;

import java.util.*;
import java.util.stream.Collectors;

import static project.restapi.service.utils.AverageGradeCalculator.getAverageGradeForAStudentInCourse;
import static project.restapi.service.utils.StudentEnrollmentValidator.isStudentInCourse;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public StudentAddResponse add(StudentAddRequest studentAddRequest) throws NullPointerException {
        if (!studentAddRequest.getName().matches(RegexValidations.FIRST_NAME_VALIDATOR)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_NAME);
        }

        if (!studentAddRequest.getUniqueCitizenNumber().matches(RegexValidations.UNIQUE_CITIZEN_NUMBER_VALIDATOR)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_UCN);
        }

        if (studentRepository.findByUniqueCitizenNumber(studentAddRequest.getUniqueCitizenNumber()).isPresent() ||
                studentRepository.findByUsername(studentAddRequest.getUsername()).isPresent()) {
            throw new ObjectAlreadyExistsException(ErrorMessages.STUDENT_EXISTS);
        }

        if (studentAddRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_PASSWORD);
        }

        Student student = new Student();
        modelMapper.map(studentAddRequest, student);
        student.getAuthorities().add(roleRepository.findByAuthority(RoleValues.STUDENT));
        student.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));

        return modelMapper.map(studentRepository.saveAndFlush(student), StudentAddResponse.class);
    }

    @Override
    public StudentAddToCourseResponse addToCourse(StudentAddToCourseRequest studentAddToCourseRequest) {
        Student student = studentRepository.findById(studentAddToCourseRequest.getStudentId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        Course course = courseRepository.findByName(studentAddToCourseRequest.getCourseName())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));

        if (isStudentInCourse(student, course)) {
            throw new IllegalArgumentException(ErrorMessages.STUDENT_ALREADY_IN_COURSE);
        }

        student.getCourses().add(course);
        StudentAddToCourseResponse studentAddToCourseResponse = modelMapper.map(studentRepository.saveAndFlush(student), StudentAddToCourseResponse.class);
        studentAddToCourseResponse.setCourseName(course.getName());
        return studentAddToCourseResponse;
    }

    @Override
    public List<CourseAllOrderedResponse> getAllInCoursesAscByAverageGradeAsc() {
        List<Course> result = new ArrayList<>();

        courseRepository.findAllOrderedByNameAsc()
                .forEach(course -> {
                    course.setStudents(course.getStudents()
                            .stream()
                            .sorted(Comparator.comparingDouble(f -> getAverageGradeForAStudentInCourse(f, course.getName())))
                            .collect(Collectors.toCollection(LinkedHashSet::new)));

                    result.add(course);
                });

        return result
                .stream()
                .map(CourseAndStudentsOrderedMapper::mapCourseAndStudents)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalAverageGrade(String studentId) {
        Student student = studentRepository.findById(Long.valueOf(studentId))
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        List<Double> averageGrades = new ArrayList<>();

        for (Course course : student.getCourses()) {
            Double averageGrade = getAverageGradeForAStudentInCourse(student, course.getName());
            if (averageGrade >= GradeValues.MIN) {
                averageGrades.add(averageGrade);
            }
        }

        return averageGrades.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(OutputMessage.EMPTY_AVERAGE_GRADE);
    }

    @Override
    public Double getTotalAverageGradeStudentSelf(Long studentId) {
        return getTotalAverageGrade(String.valueOf(studentId));
    }

    @Override
    public StudentProfileResponse getProfileData(String name) {
        Student student = studentRepository.findByUsername(name.toLowerCase())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));
        StudentProfileResponse result = modelMapper.map(student, StudentProfileResponse.class);
        result.setCoursesAttended(student.getCourses().size());

        result.setAverageGrade(
                student.getGrades()
                        .stream()
                        .mapToDouble(Grade::getValue)
                        .average()
                        .orElse(0.00)
        );
        return result;
    }

    @Override
    public List<StudentAvailableResponse> getStudentsNotInCourse(CourseAvailableStudentsRequest courseAvailableStudentsRequest) {
        List<StudentAvailableResponse> result = new ArrayList<>();

        studentRepository.findAll()
                .forEach(student -> {
                    List<Course> courses = student.getCourses();
                    boolean isEnrolled = false;
                    for (Course course : courses) {
                        if (course.getName().equalsIgnoreCase(courseAvailableStudentsRequest.getCourseName())) {
                            isEnrolled = true;
                            break;
                        }
                    }
                    if (!isEnrolled) {
                        result.add(modelMapper.map(student, StudentAvailableResponse.class));
                    }
                });

        return result;
    }

    @Override
    public StudentProfileResponse getById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        List<CourseAllResponse> courseAllResponses = student.getCourses()
                .stream()
                .map(course -> modelMapper.map(course, CourseAllResponse.class))
                .collect(Collectors.toList());

        StudentProfileResponse studentProfileResponse = modelMapper.map(student, StudentProfileResponse.class);
        studentProfileResponse.setCoursesEnrolled(courseAllResponses);

        return studentProfileResponse;
    }

}