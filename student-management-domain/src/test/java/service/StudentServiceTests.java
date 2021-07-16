//package service;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import project.restapi.domain.entities.Course;
//import project.restapi.domain.entities.Grade;
//import project.restapi.domain.entities.Student;
//import project.restapi.service.impl.StudentServiceImpl;
//import repository.CourseRepository;
//import repository.StudentRepository;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//
//public class StudentServiceTests {
//    private static final String STUDENT_NAME = "John";
//    private static final String INVALID_STUDENT_NAME = "john";
//    private static final Integer STUDENT_AGE = 19;
//    private static final String COURSE_NAME = "Java";
//    private static final String SECOND_COURSE_NAME = "C#";
//    private static final String UCN = "9951218086";
//    private static final String ID = "1";
//    private static final String INVALID_UCN = "99512180862";
//    private static final String GET_ALL_EXPECTED_OUTPUT = "JAVA\r\n" +
//            "John - 5.00";
//    private static final Double EXPECTED_AVERAGE = 5.0;
//    private static final String USERNAME = "john";
//    private static final String PASSWORD = "password";
//
//    private StudentRepository mockStudentRepository;
//    private CourseRepository mockCourseRepository;
//
//    //TODO CLI
//    private StudentServiceImpl studentService;
//
//    private Student student;
//    private Course course;
//
//    @Before
//    public void init() {
//        mockCourseRepository = Mockito.mock(CourseRepository.class);
//        mockStudentRepository = Mockito.mock(StudentRepository.class);
//
//        studentService = new StudentServiceImpl(mockStudentRepository, mockCourseRepository);
//
//        course = new Course();
//        course.setName(COURSE_NAME);
//
//        Course secondCourse = new Course();
//        secondCourse.setName(SECOND_COURSE_NAME);
//
//        Grade excellent = new Grade();
//        excellent.setValue(6.00);
//        Grade good = new Grade();
//        good.setValue(4.00);
//        Grade veryGood = new Grade();
//        veryGood.setValue(5.00);
//
//        student = new Student();
//        student.setName(STUDENT_NAME);
//        student.setGrades(new ArrayList<>());
//        student.getGrades().add(good);
//        student.getGrades().add(excellent);
//        student.getGrades().add(veryGood);
//        student.setCourses(new ArrayList<>());
//        student.getCourses().add(course);
//
//        good.setStudent(student);
//        good.setCourse(course);
//        excellent.setStudent(student);
//        excellent.setCourse(course);
//        veryGood.setStudent(student);
//        veryGood.setCourse(secondCourse);
//
//        course.setStudents(new HashSet<>());
//        course.getStudents().add(student);
//    }
//
//    @Test
//    public void add_Should_Return_False_If_Present() {
//        Mockito.when(mockStudentRepository.isPresent(UCN))
//                .thenReturn(true);
//
//        boolean result = studentService.add(STUDENT_NAME, STUDENT_AGE, UCN, USERNAME, PASSWORD);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void add_Should_Return_False_If_Name_Is_Invalid() {
//        Mockito.when(mockStudentRepository.isPresent(UCN))
//                .thenReturn(false);
//
//        boolean result = studentService.add(INVALID_STUDENT_NAME, STUDENT_AGE, UCN, USERNAME, PASSWORD);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void add_Should_Return_False_If_UCN_Is_Invalid() {
//        Mockito.when(mockStudentRepository.isPresent(UCN))
//                .thenReturn(false);
//
//        boolean result = studentService.add(STUDENT_NAME, STUDENT_AGE, INVALID_UCN, USERNAME, PASSWORD);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void add_Should_Return_True() {
//        Mockito.when(mockStudentRepository.isPresent(UCN))
//                .thenReturn(false);
//
//        Mockito.when(mockStudentRepository.save(any(Student.class)))
//                .thenReturn(true);
//
//        boolean result = studentService.add(STUDENT_NAME, STUDENT_AGE, UCN, USERNAME, PASSWORD);
//
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void addToCourse_Should_Return_False() {
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(null);
//
//        Mockito.when(mockStudentRepository.findByUCN(UCN))
//                .thenReturn(null);
//
//        boolean result = studentService.addToCourse(UCN, COURSE_NAME);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void addToCourse_Should_Return_True() {
//        student.setCourses(new ArrayList<>());
//
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(course);
//
//        Mockito.when(mockStudentRepository.findByUCN(UCN))
//                .thenReturn(student);
//
//        Mockito.when(mockStudentRepository.addToCourse(student, course))
//                .thenReturn(true);
//
//        boolean result = studentService.addToCourse(UCN, COURSE_NAME);
//
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void getAllInCoursesAscByAverageGradeAsc_Should_Return_Correct_Result() {
//        List<Course> courses = new ArrayList<>();
//        courses.add(course);
//
//        Mockito.when(mockCourseRepository.findAllOrderedByNameAsc())
//                .thenReturn(courses);
//
//        String result = studentService.printAllInCoursesAscByAverageGradeAsc();
//
//        Assert.assertEquals(GET_ALL_EXPECTED_OUTPUT, result);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void getTotalAverageGrade_Should_Throw_If_User_Is_Null() {
//        Mockito.when(mockStudentRepository.findByUCN(UCN))
//                .thenReturn(null);
//
//        studentService.getTotalAverageGrade(UCN);
//    }
//
//    @Test
//    public void getTotalAverageGrade_Should_Return_Correctly() {
//        Mockito.when(mockStudentRepository.findById(Long.parseLong(ID)))
//                .thenReturn(student);
//
//        Double result = studentService.getTotalAverageGrade(ID);
//
//        Assert.assertEquals(EXPECTED_AVERAGE, result);
//    }
//
//    @Test
//    public void addToCourse_Should_Return_False_If_Already_Signed_In() {
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(course);
//
//        Mockito.when(mockStudentRepository.findByUCN(UCN))
//                .thenReturn(student);
//
//        Mockito.when(mockStudentRepository.addToCourse(student, course))
//                .thenReturn(true);
//
//        boolean result = studentService.addToCourse(UCN, COURSE_NAME);
//
//        Assert.assertFalse(result);
//    }
//}