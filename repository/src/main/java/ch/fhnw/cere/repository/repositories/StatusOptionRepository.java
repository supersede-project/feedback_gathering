
package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.StatusOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StatusOptionRepository extends JpaRepository<StatusOption, Long> {
}
