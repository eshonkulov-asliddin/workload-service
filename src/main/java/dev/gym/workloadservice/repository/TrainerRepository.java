package dev.gym.workloadservice.repository;

import dev.gym.workloadservice.dto.TrainerReportProjection;
import dev.gym.workloadservice.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByUsername(String username);

    @Query(nativeQuery = true)
    List<TrainerReportProjection> calculateMonthlyReportByUsername(String username);

    @Query(value = "SELECT CASE WHEN COUNT(tr) > 0 THEN true ELSE false END FROM Trainer tr WHERE tr.username = ?1")
    boolean existsByUsername(String username);

}
