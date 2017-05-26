package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

}
