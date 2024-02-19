package dev.gym.workloadservice.repository;

import dev.gym.workloadservice.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {

}
