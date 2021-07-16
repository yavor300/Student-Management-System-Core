package project.restapi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.restapi.constants.ErrorMessages;
import project.restapi.constants.GradeValues;
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

import static project.restapi.service.utils.StudentEnrollmentValidator.isStudentInCourse;

@Service
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public GradeServiceImpl(GradeRepository gradeRepository, StudentRepository studentRepository, CourseRepository courseRepository, ModelMapper modelMapper) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public GradeAddResponse addGrade(GradeAddRequest gradeAddRequest) {
        if (!(gradeAddRequest.getValue() >= GradeValues.MIN && gradeAddRequest.getValue() <= GradeValues.MAX)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_GRADE_VALUE);
        }

        Student student = studentRepository.findById(gradeAddRequest.getStudentId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        Course course = courseRepository.findByName(gradeAddRequest.getCourseName())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorMessages.COURSE_DOES_NOT_EXIST));

        if (!isStudentInCourse(student, course)) {
            throw new IllegalArgumentException(ErrorMessages.STUDENT_NOT_IN_COURSE);
        }

        Grade grade = new Grade(gradeAddRequest.getValue(), student, course);
        return modelMapper.map(gradeRepository.saveAndFlush(grade), GradeAddResponse.class);
    }
}
