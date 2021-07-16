package project.restapi.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Grade;
import project.restapi.domain.entities.Student;
import project.restapi.domain.models.api.request.GradeAddRequest;
import project.restapi.domain.models.api.response.GradeAddResponse;
import project.restapi.exceptions.ObjectNotFoundException;
import project.restapi.repository.CourseRepository;
import project.restapi.repository.GradeRepository;
import project.restapi.repository.StudentRepository;
import project.restapi.service.GradeService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


public class GradeServiceTests {
    public static final Double INVALID_GRADE_VALUE = 1.0;
    public static final Double GRADE_VALUE = 6.0;
    private static final Long ID = 1L;
    private static final String COURSE_NAME = "Java";

    private GradeRepository mockGradeRepository;
    private StudentRepository mockStudentRepository;
    private CourseRepository mockCourseRepository;
    private GradeService gradeService;

    @Before
    public void init() {
        mockGradeRepository = Mockito.mock(GradeRepository.class);
        mockStudentRepository = Mockito.mock(StudentRepository.class);
        mockCourseRepository = Mockito.mock(CourseRepository.class);

        gradeService = new GradeServiceImpl(mockGradeRepository, mockStudentRepository, mockCourseRepository, new ModelMapper());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addGrade_Should_Throw_IllegalArgumentException() {
        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(INVALID_GRADE_VALUE);

        gradeService.addGrade(gradeAddRequest);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void addGrade_Should_Throw_ObjectNotFoundException_If_Student_Null() {
        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(GRADE_VALUE);
        gradeAddRequest.setStudentId(ID);

        Mockito.when(mockStudentRepository.findById(gradeAddRequest.getStudentId()))
                .thenReturn(Optional.empty());

        gradeService.addGrade(gradeAddRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addGrade_Should_Throw_IllegalArgumentException_If_Student_Not_In_Course() {
        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(GRADE_VALUE);
        gradeAddRequest.setStudentId(ID);
        gradeAddRequest.setCourseName(COURSE_NAME);

        Student student = new Student();
        Mockito.when(mockStudentRepository.findById(gradeAddRequest.getStudentId()))
                .thenReturn(Optional.of(student));

        Course course = new Course();
        course.setName(COURSE_NAME);
        Mockito.when(mockCourseRepository.findByName(gradeAddRequest.getCourseName()))
                .thenReturn(Optional.of(course));

        gradeService.addGrade(gradeAddRequest);
    }

    @Test
    public void addGrade_Should_Return_Correctly() {
        GradeAddRequest gradeAddRequest = new GradeAddRequest();
        gradeAddRequest.setValue(GRADE_VALUE);
        gradeAddRequest.setStudentId(ID);
        gradeAddRequest.setCourseName(COURSE_NAME);

        Student student = new Student();
        student.setId(ID);
        Mockito.when(mockStudentRepository.findById(gradeAddRequest.getStudentId()))
                .thenReturn(Optional.of(student));

        Course course = new Course();
        course.setId(ID);
        course.setName(COURSE_NAME);
        Mockito.when(mockCourseRepository.findByName(gradeAddRequest.getCourseName()))
                .thenReturn(Optional.of(course));

        Grade grade = new Grade();
        grade.setId(ID);
        grade.setCourse(course);
        grade.setStudent(student);
        grade.setValue(GRADE_VALUE);
        Mockito.when(mockGradeRepository.saveAndFlush(any(Grade.class)))
                .thenReturn(grade);

        student.getCourses().add(course);
        GradeAddResponse gradeAddResponse = gradeService.addGrade(gradeAddRequest);

        Assert.assertEquals(ID, gradeAddResponse.getId());
        Assert.assertEquals(ID, gradeAddResponse.getStudentId());
        Assert.assertEquals(GRADE_VALUE, gradeAddResponse.getValue());
    }
}
