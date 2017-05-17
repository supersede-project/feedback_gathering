package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.models.TriggerType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "configurations", path = "configurations")
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {
    List<Configuration> findByName(@Param("name") String name);
    List<Configuration> findByType(@Param("type") TriggerType type);
}