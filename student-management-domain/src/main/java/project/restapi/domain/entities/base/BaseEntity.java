package project.restapi.domain.entities.base;

import javax.persistence.*;

/**
 * The BaseModel is the base class for all models in the application.
 * It contains the primary key for each entity.
 */
@MappedSuperclass
public abstract class BaseEntity {
    private Long id;

    public BaseEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
