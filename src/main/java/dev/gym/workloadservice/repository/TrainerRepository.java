package dev.gym.workloadservice.repository;

import dev.gym.workloadservice.dto.MonthlyReportProjection;
import dev.gym.workloadservice.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByUsername(String username);

    @Query(value = """
        SELECT 
            tr.username, 
            tr.firstname as firstName, 
            tr.lastname as lastName, 
            tr.is_active as isActive,
            EXTRACT(YEAR FROM tg.training_date) AS `year`,
            EXTRACT(MONTH FROM tg.training_date) AS `month`,
            SUM(tg.training_duration) AS totalTrainingDuration
        FROM 
            Trainer tr
            JOIN Training tg ON tr.id = tg.trainer_id
        WHERE 
            tr.username= ?1
        GROUP BY 
            tr.username, `year`, `month`
        ORDER BY 
                `year` DESC, `month` DESC
        """,
        nativeQuery = true)
    List<MonthlyReportProjection> calculateMonthlyReportByUsername(String username);

}
