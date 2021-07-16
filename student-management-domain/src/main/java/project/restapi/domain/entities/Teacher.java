package project.restapi.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;
import project.restapi.domain.entities.base.BaseEntity;
import project.restapi.domain.entities.enums.Degree;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Teacher model holds information about the name of the teacher, her/his unique citizen number, the degree, and also the courses,
 * in which the teacher participates.
 */
@Entity
@Table(name = "teachers", indexes = @Index(
        name = "idx_teachers_unique_citizen_number",
        columnList = "unique_citizen_number",
        unique = true
))
public class Teacher extends BaseEntity implements UserDetails {
    private String name;
    private String uniqueCitizenNumber;
    private String username;
    private String password;
    private Degree degree;
    private List<Course> courses;
    private Set<Role> authorities;

    public Teacher() {
        this.authorities = new HashSet<>();
    }

    public Teacher(String name, String username, String password, String uniqueCitizenNumber, Degree degree) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.uniqueCitizenNumber = uniqueCitizenNumber;
        this.degree = degree;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    @OneToMany(mappedBy = "teacher")
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
            name = "teachers_roles",
            joinColumns = { @JoinColumn(name = "teacher_id") },
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