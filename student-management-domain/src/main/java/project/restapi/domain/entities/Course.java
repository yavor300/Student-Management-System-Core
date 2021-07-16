package project.restapi.domain.entities;

import project.restapi.domain.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.*;

/**
 * The Course model holds information about the courses in the application.
 * It stores the name of the course, the total hours, students enrolled, the teacher, and also the grades of the students.
 */
@Entity
@Table(name = "courses", indexes = @Index(
        name = "idx_course_name",
        columnList = "name",
        unique = true
))
public class Course extends BaseEntity {
    private String name;
    private Double totalHours;
    private Set<Student> students;
    private Teacher teacher;
    private List<Grade> grades;

    public Course() {
        this.students = new LinkedHashSet<>();
        this.grades = new ArrayList<>();
    }

    public Course(String name, Double totalHours) {
        this.name = name;
        this.totalHours = totalHours;
        this.students = new HashSet<>();
        this.grades = new ArrayList<>();
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "total_hours", nullable = false)
    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    @ManyToMany(mappedBy = "courses")
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @OneToMany(mappedBy = "course")
    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}