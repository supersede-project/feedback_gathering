package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.Parameter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "parameters", path = "parameters")
public interface ParameterRepository extends CrudRepository<Parameter, Long> {

}