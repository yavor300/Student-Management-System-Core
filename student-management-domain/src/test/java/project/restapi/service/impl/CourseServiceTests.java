package project.restapi.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import project.restapi.domain.entities.Course;
import project.restapi.domain.entities.Teacher;
import project.restapi.domain.models.api.request.CourseAddRequest;
import project.restapi.domain.models.api.request.CourseAddTeacherRequest;
import project.restapi.domain.models.api.response.CourseAddResponse;
import project.restapi.domain.models.api.response.CourseAddTeacherResponse;
import project.restapi.domain.models.api.response.CourseAllResponse;
import project.restapi.exceptions.ObjectAlreadyExistsException;
import project.restapi.exceptions.ObjectNotFoundException;
import project.restapi.repository.CourseRepository;
import project.restapi.repository.TeacherRepository;
import project.restapi.service.CourseService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class CourseServiceTests {
    private static final String COURSE_NAME = "Java";
    public static final Double TOTAL_HOURS = 90.0;
    public static final Long ID = 1L;

    private CourseRepository mockCourseRepository;
    private TeacherRepository mockTeacherRepository;
    private CourseService courseService;

    @Before
    public void init() {
        mockCourseRepository = Mockito.mock(CourseRepository.class);
        mockTeacherRepository = Mockito.mock(TeacherRepository.class);
        courseService = new CourseServiceImpl(mockCourseRepository,mockTeacherRepository, new ModelMapper());
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void add_Should_Throw_ObjectAlreadyExistsException() {
        CourseAddRequest courseAddRequest = new CourseAddRequest();
        courseAddRequest.setName(COURSE_NAME);

        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
                .thenReturn(Optional.of(new Course()));

        courseService.add(courseAddRequest);
    }

    @Test
    public void add_Should_Work_Correctly() {
        CourseAddRequest courseAddRequest = new CourseAddRequest();
        courseAddRequest.setName(COURSE_NAME);
        courseAddRequest.setTotalHours(TOTAL_HOURS);

        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
                .thenReturn(Optional.empty());

        Course course = new Course();
        course.setId(ID);
        course.setName(COURSE_NAME);
        course.setTotalHours(TOTAL_HOURS);

        Mockito.when(mockCourseRepository.saveAndFlush(any(Course.class)))
                .thenReturn(course);

        CourseAddResponse courseAddResponse = courseService.add(courseAddRequest);

        Assert.assertEquals(ID, courseAddResponse.getId());
        Assert.assertEquals(COURSE_NAME, courseAddResponse.getName());
        Assert.assertEquals(TOTAL_HOURS, courseAddResponse.getTotalHours());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void addTeacher_Should_Throw_ObjectNotFoundException_When_Teacher_Is_Null() {
        CourseAddTeacherRequest courseAddTeacherRequest = new CourseAddTeacherRequest();
        courseAddTeacherRequest.setCourseName(COURSE_NAME);
        courseAddTeacherRequest.setTeacherId(ID);

        Mockito.when(mockTeacherRepository.findById(ID))
                    .thenReturn(Optional.empty());

        courseService.addTeacher(courseAddTeacherRequest);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void addTeacher_Should_Throw_ObjectNotFoundException_When_Course_Is_Null() {
        CourseAddTeacherRequest courseAddTeacherRequest = new CourseAddTeacherRequest();
        courseAddTeacherRequest.setCourseName(COURSE_NAME);
        courseAddTeacherRequest.setTeacherId(ID);

        Mockito.when(mockTeacherRepository.findById(ID))
                .thenReturn(Optional.of(new Teacher()));

        Mockito.when(mockCourseRepository.findByName(courseAddTeacherRequest.getCourseName()))
                .thenReturn(Optional.empty());

        courseService.addTeacher(courseAddTeacherRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTeacher_Should_Throw_IllegalArgumentException_When_Course_Has_Teacher() {
        CourseAddTeacherRequest courseAddTeacherRequest = new CourseAddTeacherRequest();
        courseAddTeacherRequest.setCourseName(COURSE_NAME);
        courseAddTeacherRequest.setTeacherId(ID);

        Mockito.when(mockTeacherRepository.findById(ID))
                .thenReturn(Optional.of(new Teacher()));

        Course course = new Course();
        course.setTeacher(new Teacher());
        Mockito.when(mockCourseRepository.findByName(courseAddTeacherRequest.getCourseName()))
                .thenReturn(Optional.of(course));

        courseService.addTeacher(courseAddTeacherRequest);
    }


    @Test
    public void addTeacher_Should_Work_Correctly() {
        CourseAddTeacherRequest courseAddTeacherRequest = new CourseAddTeacherRequest();
        courseAddTeacherRequest.setCourseName(COURSE_NAME);
        courseAddTeacherRequest.setTeacherId(ID);

        Teacher teacher = new Teacher();
        teacher.setId(ID);
        Mockito.when(mockTeacherRepository.findById(ID))
                .thenReturn(Optional.of(teacher));

        Course course = new Course();
        course.setName(COURSE_NAME);
        Mockito.when(mockCourseRepository.findByName(courseAddTeacherRequest.getCourseName()))
                .thenReturn(Optional.of(course));

        Mockito.when(mockCourseRepository.saveAndFlush(any(Course.class)))
                .thenReturn(course);

        CourseAddTeacherResponse courseAddTeacherResponse = courseService.addTeacher(courseAddTeacherRequest);

        Assert.assertEquals(ID.toString(), courseAddTeacherResponse.getTeacherId());
        Assert.assertEquals(COURSE_NAME, courseAddTeacherResponse.getName());
    }

    @Test
    public void getAllCourses_Should_Return_Correct() {
        Mockito.when(mockCourseRepository.findAll())
                .thenReturn(List.of(new Course(), new Course()));

        List<CourseAllResponse> courseAllResponses = courseService.getAllCourses();

        Assert.assertEquals(2, courseAllResponses.size());
    }

    @Test
    public void seedCourses_Should_Work_Correctly() {
        Mockito.when(mockCourseRepository.count())
                .thenReturn(0L);

        Mockito.when(mockCourseRepository.save(any(Course.class)))
                .thenReturn(new Course());

        courseService.seedCourses();

        Mockito.verify(mockCourseRepository, times(7)).save(any(Course.class));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getByName_Should_Throw_ObjectNotFoundException() {
        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
                .thenReturn(Optional.empty());

        courseService.getByName(COURSE_NAME);
    }

    @Test
    public void getByName_Should_Return_Correctly() {
        Course course = new Course();
        course.setId(ID);

        Mockito.when(mockCourseRepository.findByName(COURSE_NAME))
                .thenReturn(Optional.of(course));

        Course courseResult = courseService.getByName(COURSE_NAME);

        Assert.assertEquals(ID, courseResult.getId());
    }
}
