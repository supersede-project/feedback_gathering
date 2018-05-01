package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.AndroidUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AndroidUserRepository extends JpaRepository<AndroidUser, Long> {
}
