package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    List<Parameter> findByMechanismId(@Param("mechanism_id") long mechanismId);
}
