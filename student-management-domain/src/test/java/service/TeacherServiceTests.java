//package service;
//
//import project.restapi.domain.entities.Teacher;
//import project.restapi.domain.entities.enums.Degree;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import repository.TeacherRepository;
//import service.impl.cli.TeacherServiceImpl;
//
//import static org.mockito.ArgumentMatchers.any;
//
//public class TeacherServiceTests {
//    private static final String TEACHER_NAME = "Bob";
//    private static final String INVALID_TEACHER_NAME = "bob";
//    private static final String UCN = "9951218086";
//    private static final String INVALID_UCN = "99512180861";
//    private static final String USERNAME = "bob";
//    private static final String PASSWORD = "password";
//
//    private TeacherRepository mockTeacherRepository;
//
//    private TeacherService teacherService;
//
//    @Before
//    public void init() {
//        mockTeacherRepository = Mockito.mock(TeacherRepository.class);
//
//        teacherService = new TeacherServiceImpl(mockTeacherRepository);
//    }
//
//    @Test
//    public void add_Should_Return_False_If_Present() {
//        Mockito.when(mockTeacherRepository.isPresent(UCN))
//                .thenReturn(true);
//
//        boolean result = teacherService.add(TEACHER_NAME, USERNAME, PASSWORD, UCN, Degree.PHD);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void add_Should_Return_False_If_Name_Is_Invalid() {
//        Mockito.when(mockTeacherRepository.isPresent(UCN))
//                .thenReturn(false);
//
//        boolean result = teacherService.add(INVALID_TEACHER_NAME, USERNAME, PASSWORD, UCN, Degree.MASTERS);
//
//        Assert.assertFalse(result);
//    }
//
//
//        @Test
//    public void add_Should_Return_False_If_UCN_Is_Invalid() {
//        Mockito.when(mockTeacherRepository.isPresent(UCN))
//                .thenReturn(false);
//
//        boolean result = teacherService.add(TEACHER_NAME, USERNAME, PASSWORD, INVALID_UCN, Degree.MASTERS);
//
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void add_Should_Return_True() {
//        Mockito.when(mockTeacherRepository.isPresent(UCN))
//                .thenReturn(false);
//
//        Mockito.when(mockTeacherRepository.save(any(Teacher.class)))
//                .thenReturn(true);
//
//        boolean result = teacherService.add(TEACHER_NAME, USERNAME, PASSWORD, UCN, Degree.PHD);
//
//        Assert.assertTrue(result);
//    }
//}
