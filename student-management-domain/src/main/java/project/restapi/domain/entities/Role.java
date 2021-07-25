package project.restapi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import project.restapi.domain.entities.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {
    private String authority;
    @JsonIgnore
    private Set<Student> students;
    @JsonIgnore
    private Set<Teacher> teachers;

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    @Column(unique = true)
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @ManyToMany(mappedBy = "authorities")
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @ManyToMany(mappedBy = "authorities")
    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }
}