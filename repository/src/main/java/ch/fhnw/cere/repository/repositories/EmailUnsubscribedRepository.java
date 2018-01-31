package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.EmailUnsubscribed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Aydinli on 31.01.2018.
 */
@Repository
public interface EmailUnsubscribedRepository extends JpaRepository<EmailUnsubscribed, Long> {
    EmailUnsubscribed findByEnduserId(@Param("userId") long userId);
    EmailUnsubscribed findByEmail(@Param("email") String email);
}
