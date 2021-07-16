package project.restapi.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;
import project.restapi.domain.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.*;

/**
 * The Student model holds information about the name of the student, is age, unique citizen number, the courses he/she is enrolled in,
 * and also his/her grades.
 */
@Entity
@Table(name = "students", indexes = @Index(
        name = "idx_students_unique_citizen_number",
        columnList = "unique_citizen_number",
        unique = true
))
public class Student extends BaseEntity implements UserDetails {
    private String name;
    private Integer age;
    private String uniqueCitizenNumber;
    private String username;
    private String password;
    private List<Course> courses;
    private List<Grade> grades;
    private Set<Role> authorities;

    public Student() {
        this.courses = new ArrayList<>();
        this.grades = new ArrayList<>();
        this.authorities = new HashSet<>();
    }

    public Student(String name, Integer age, String uniqueCitizenNumber, String username, String password) {
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
        this.uniqueCitizenNumber = uniqueCitizenNumber;
        this.courses = new ArrayList<>();
        this.grades = new ArrayList<>();
        this.authorities = new HashSet<>();
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @ManyToMany
    @JoinTable(
            name = "students_courses",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Column(name = "unique_citizen_number", unique = true, nullable = false)
    public String getUniqueCitizenNumber() {
        return uniqueCitizenNumber;
    }

    public void setUniqueCitizenNumber(String uniqueCitizenNumber) {
        this.uniqueCitizenNumber = uniqueCitizenNumber;
    }

    @OneToMany(mappedBy = "student")
    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    @Column(nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "students_roles",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }
}