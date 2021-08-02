package project.restapi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.restapi.constants.ErrorMessages;
import project.restapi.constants.GradeValues;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.models.api.request.CourseAddRequest;
import project.restapi.domain.models.api.request.CourseAddTeacherRequest;
import project.restapi.domain.models.api.response.*;
import project.restapi.exceptions.ObjectAlreadyExistsException;
import project.restapi.exceptions.ObjectNotFoundException;
import project.restapi.repository.CourseRepository;
import project.restapi.repository.TeacherRepository;
import project.restapi.service.CourseService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, TeacherRepository teacherRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CourseAddResponse add(CourseAddRequest courseAddRequest) {
        if (courseAddRequest.getName().trim().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.COURSE_NAME_CANNOT_BE_EMPTY);
        }

        if (courseRepository.findByName(courseAddRequest.getName()).isPresent()) {
            throw new ObjectAlreadyExistsException(ErrorMessages.COURSE_ALREADY_EXISTS);
        }
        Course course = new Course(courseAddRequest.getName(), courseAddRequest.getTotalHours());
        return modelMapper.map(courseRepository.saveAndFlush(course), CourseAddResponse.class);
    }

    @Override
    public CourseAddTeacherResponse addTeacher(CourseAddTeacherRequest courseAddTeacherRequest) {
        Teacher teacher = teacherRepository.findById(courseAddTeacherRequest.getTeacherId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.TEACHER_NOT_FOUND));

        Course course = courseRepository.findByName(courseAddTeacherRequest.getCourseName())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));

        if (course.getTeacher() != null) {
            throw new IllegalArgumentException(ErrorMessages.COURSE_ALREADY_HAS_TEACHER);
        }

        course.setTeacher(teacher);
        return modelMapper.map(courseRepository.saveAndFlush(course), CourseAddTeacherResponse.class);
    }

    @Override
    public List<CourseAllResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(course -> {
                    CourseAllResponse courseAllResponse = modelMapper.map(course, CourseAllResponse.class);
                    courseAllResponse.setAverageGrade(
                            course.getGrades()
                            .stream()
                            .mapToDouble(Grade::getValue)
                            .average()
                            .orElse(GradeValues.EMPTY_AVERAGE_GRADE));
                    return courseAllResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void seedCourses() {
        if (courseRepository.count() == 0) {
            Course[] courses = {
                    new Course("Java", 100.0),
                    new Course("C#", 90.0),
                    new Course("Python", 80.0),
                    new Course("JavaScript", 200.0),
                    new Course("C", 56.0),
                    new Course("Ruby", 78.1),
                    new Course("C++", 95.2)
            };

            Arrays.stream(courses).
                    forEach(courseRepository::save);

        }
    }

    @Override
    public CourseTeacherResponse getTeacher(String courseName) {
        Course course = courseRepository.findByName(courseName)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));

        CourseTeacherResponse courseTeacherResponse = new CourseTeacherResponse();
        if (course.getTeacher() == null) {
            courseTeacherResponse.setTeacherName(null);
            return courseTeacherResponse;
        }
        courseTeacherResponse.setTeacherName(course.getTeacher().getName());

        return courseTeacherResponse;
    }

    @Override
    public CourseAllResponse getAllStudents(String courseName) {
        Course course = courseRepository.findByName(courseName)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));
        return modelMapper.map(course, CourseAllResponse.class);
    }

    @Override
    public CourseStudentGradesResponse getAllStudentsGrades(String courseName) {
        Course course = courseRepository.findByName(courseName)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));

        return modelMapper.map(course, CourseStudentGradesResponse.class);
    }

    @Override
    public Double getAverageGrade(String courseName) {
        Course course = courseRepository.findByName(courseName)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));

        return course.getGrades()
                .stream()
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public CourseAddTeacherResponse deleteTeacher(CourseDeleteTeacherRequest courseDeleteTeacherRequest) {
        Course course = courseRepository.findByName(courseDeleteTeacherRequest.getCourseName())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));

        if (course.getTeacher() == null) {
            throw new IllegalArgumentException(ErrorMessages.COURSE_DOES_NOT_HAVE_A_TEACHER);
        }

        course.setTeacher(null);
        return modelMapper.map(courseRepository.saveAndFlush(course), CourseAddTeacherResponse.class);
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));
    }
}
