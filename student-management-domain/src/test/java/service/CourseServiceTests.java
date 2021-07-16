//package service;
//
//import project.restapi.domain.entities.Course;
//import project.restapi.domain.entities.Grade;
//import project.restapi.domain.entities.Student;
//import project.restapi.domain.entities.Teacher;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import repository.CourseRepository;
//import repository.TeacherRepository;
//import service.impl.cli.CourseServiceImpl;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//
//public class CourseServiceTests {
//    private static final String COURSE_NAME = "Java";
//    private static final String TEACHER_NAME = "Bob";
//    private static final String STUDENT_NAME = "Mark";
//    private static final String UCN = "9933442929";
//    private static final Double COURSE_TOTAL_HOURS = 300.0;
//    private static final String GET_ALL_EXPECTED_OUTPUT = "Java\r\n" +
//            "Bob (Teacher)\r\n" +
//            "Mark";
//    private static final String GET_STUDENTS_AVG_GRADE_EXPECTED_OUTPUT = "Java\r\n" +
//            "Mark - 4.00";
//    private static final String COURSE_NULL_ERROR_MESSAGE = "Course cannot be found!";
//
//    private CourseRepository mockCourseRepository;
//    private TeacherRepository mockTeacherRepository;
//
//    private CourseServiceImpl courseService;
//
//    private Course course;
//    private Teacher teacher;
//
//    @Before
//    public void init() {
//        mockCourseRepository = Mockito.mock(CourseRepository.class);
//        mockTeacherRepository = Mockito.mock(TeacherRepository.class);
//        courseService = new CourseServiceImpl(mockCourseRepository, mockTeacherRepository);
//
//        Grade poor = new Grade();
//        poor.setValue(2.00);
//        Grade excellent = new Grade();
//        excellent.setValue(6.00);
//
//        Student student = new Student();
//        student.setName(STUDENT_NAME);
//        student.setGrades(new ArrayList<>());
//        student.getGrades().add(poor);
//        student.getGrades().add(excellent);
//
//        teacher = new Teacher();
//        teacher.setName(TEACHER_NAME);
//
//        course = new Course();
//        course.setName(COURSE_NAME);
//        course.setStudents(new HashSet<>());
//        course.getStudents().add(student);
//        course.setTeacher(teacher);
//
//        poor.setCourse(course);
//        poor.setStudent(student);
//        excellent.setCourse(course);
//        excellent.setStudent(student);
//    }
//
//    @Test
//    public void add_Should_Add_The_Course_And_Return_True() {
//        Mockito.when(mockCourseRepository.isPresent(COURSE_NAME))
//                .thenReturn(false);
//
//        Mockito.when(mockCourseRepository.save(any(Course.class)))
//                .thenReturn(true);
//
//        boolean result = courseService.add(COURSE_NAME, COURSE_TOTAL_HOURS);
//
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void add_Should_Return_False_If_The_CourseName_Is_Present() {
//        Mockito.when(mockCourseRepository.isPresent(COURSE_NAME))
//                .thenReturn(true);
//
//        boolean result = courseService.add(COURSE_NAME, COURSE_TOTAL_HOURS);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void add_Teacher_Should_Add_Teacher_To_TheCourse() {
//        course.setTeacher(null);
//
//        Mockito.when(mockTeacherRepository.findByUCN(UCN))
//                .thenReturn(teacher);
//
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(course);
//
//        Mockito.when(mockCourseRepository.addTeacher(teacher, course))
//                .thenReturn(true);
//
//        boolean result = courseService.addTeacher(UCN, COURSE_NAME);
//
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void add_Teacher_Should_Return_False_If_Entities_Are_NULL() {
//        Mockito.when(mockTeacherRepository.findByUCN(UCN))
//                .thenReturn(null);
//
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(null);
//
//        Mockito.when(mockCourseRepository.addTeacher(teacher, course))
//                .thenReturn(false);
//
//        boolean result = courseService.addTeacher(UCN, COURSE_NAME);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void add_Teacher_Should_Return_False_If_There_Is_Teacher() {
//        Mockito.when(mockTeacherRepository.findByUCN(UCN))
//                .thenReturn(null);
//
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(null);
//
//        Mockito.when(mockCourseRepository.addTeacher(teacher, course))
//                .thenReturn(false);
//
//        boolean result = courseService.addTeacher(UCN, COURSE_NAME);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void getAllCourses_Should_Return_Correct_String() {
//        List<Course> courses = new ArrayList<>();
//        courses.add(course);
//
//        Mockito.when(mockCourseRepository.findAll())
//                .thenReturn(courses);
//
//        String result = courseService.printAllCourses();
//        Assert.assertEquals(GET_ALL_EXPECTED_OUTPUT, result);
//    }
//
//    @Test
//    public void getAvgGradeOfStudents_Should_Return_Correct_String() {
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(course);
//
//        String result = courseService.printAvgGradeOfStudents(COURSE_NAME);
//
//        Assert.assertEquals(GET_STUDENTS_AVG_GRADE_EXPECTED_OUTPUT, result);
//    }
//
//    @Test
//    public void getAvgGradeOfStudents_Should_Return_Message_If_Student_Is_Null() {
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(null);
//
//        String result = courseService.printAvgGradeOfStudents(COURSE_NAME);
//
//        Assert.assertEquals(COURSE_NULL_ERROR_MESSAGE, result);
//    }
//
//    @Test
//    public void getByName_Should_Return_Correct_Course() {
//        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
//                .thenReturn(course);
//
//        Course course = courseService.getByName(COURSE_NAME);
//
//        Assert.assertNotNull(course);
//    }
//}
