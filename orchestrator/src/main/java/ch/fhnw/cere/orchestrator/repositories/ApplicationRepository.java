package ch.fhnw.cere.orchestrator.repositories;


import java.util.List;

import ch.fhnw.cere.orchestrator.models.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


@Repository
@RepositoryRestResource(collectionResourceRel = "applications", path = "applications", excerptProjection = ApplicationProjection.class)
public interface ApplicationRepository extends CrudRepository<Application, Long> {
    List<Application> findByName(@Param("name") String name);
}
