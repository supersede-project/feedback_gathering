package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.models.MechanismType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "mechanisms", path = "mechanisms")
public interface MechanismRepository extends CrudRepository<Mechanism, Long> {
    List<Mechanism> findByType(@Param("type")MechanismType type);
}