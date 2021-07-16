package project.restapi.domain.entities;

import project.restapi.domain.entities.base.BaseEntity;

import javax.persistence.*;

/**
 * The Grade model holds information about the grades of the student in a certain course.
 */
@Entity
@Table(name = "grades")
public class Grade extends BaseEntity {
    private Double value;
    private Student student;
    private Course course;

    public Grade() {
    }

    public Grade(Double value, Student student, Course course) {
        this.value = value;
        this.student = student;
        this.course = course;
    }

    @Column(nullable = false)
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}