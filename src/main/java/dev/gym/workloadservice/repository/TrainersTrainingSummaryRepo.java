package dev.gym.workloadservice.repository;

import dev.gym.workloadservice.model.TrainersTrainingSummary;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainersTrainingSummaryRepo extends CrudRepository<TrainersTrainingSummary, String> {

    @Query("{ trainerUsername : '?0' }")
    Optional<TrainersTrainingSummary> findSummaryByTrainerUsername(String trainerUsername);

}
