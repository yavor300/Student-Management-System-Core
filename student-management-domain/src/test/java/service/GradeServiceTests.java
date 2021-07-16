//package service;
//
//import project.restapi.domain.entities.Course;
//import project.restapi.domain.entities.Grade;
//import project.restapi.domain.entities.Student;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import repository.CourseRepository;
//import repository.GradeRepository;
//import repository.StudentRepository;
//import service.impl.cli.GradeServiceImpl;
//
//import java.util.ArrayList;
//
//import static org.mockito.ArgumentMatchers.any;
//
//public class GradeServiceTests {
//    private static final String STUDENT_NAME = "John";
//    private static final String COURSE_NAME = "Java";
//    private static final Double EXCELLENT_VALUE = 6.0;
//    private static final Double INVALID_VALUE = 7.0;
//    private static final String UCN = "9933442929";
//
//    private CourseRepository mockCourseRepository;
//    private StudentRepository mockStudentRepository;
//    private GradeRepository mockGradeRepository;
//
//    private GradeService gradeService;
//
//    private Student student;
//    private Course course;
//
//    @Before
//    public void init() {
//        mockCourseRepository = Mockito.mock(CourseRepository.class);
//        mockStudentRepository = Mockito.mock(StudentRepository.class);
//        mockGradeRepository = Mockito.mock(GradeRepository.class);
//
//        gradeService = new GradeServiceImpl(mockGradeRepository, mockStudentRepository, mockCourseRepository);
//
//        student = new Student();
//        student.setName(STUDENT_NAME);
//        student.setGrades(new ArrayList<>());
//        student.setCourses(new ArrayList<>());
//
//        course = new Course();
//        course.setName(COURSE_NAME);
//
//        Grade grade = new Grade();
//        grade.setValue(6.00);
//        grade.setCourse(course);
//        grade.setStudent(student);
//    }
//
//    @Test
//    public void addGrade_Should_Return_False_When_Invalid() {
//        boolean result = gradeService.addGrade(INVALID_VALUE, UCN, COURSE_NAME);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void addGrade_Should_Return_True() {
//        student.getCourses().add(course);
//
//        Mockito.when(mockStudentRepository.findByUCN(UCN))
//                .thenReturn(student);
//
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(course);
//
//        Mockito.when(mockGradeRepository.save(any(Grade.class)))
//                .thenReturn(true);
//
//        boolean result = gradeService.addGrade(EXCELLENT_VALUE, UCN, COURSE_NAME);
//
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void addGrade_Should_Return_False_If_Student_Not_In_Course() {
//        Mockito.when(mockStudentRepository.findByUCN(UCN))
//                .thenReturn(student);
//
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(course);
//
//        boolean result = gradeService.addGrade(EXCELLENT_VALUE, UCN, COURSE_NAME);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void addGrade_Should_Return_False_If_Student_And_Course_Are_Null() {
//        Mockito.when(mockStudentRepository.findByUCN(UCN))
//                .thenReturn(null);
//
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(null);
//
//        boolean result = gradeService.addGrade(EXCELLENT_VALUE, UCN, COURSE_NAME);
//
//        Assert.assertFalse(result);
//    }
//}
