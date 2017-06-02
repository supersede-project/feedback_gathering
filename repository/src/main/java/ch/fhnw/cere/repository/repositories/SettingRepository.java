package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Setting findByApplicationId(@Param("applicationId") long applicationId);
}
