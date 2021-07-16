package project.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.restapi.domain.entities.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
}
