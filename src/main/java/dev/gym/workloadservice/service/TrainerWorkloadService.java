package dev.gym.workloadservice.service;

import dev.gym.workloadservice.dto.TrainerWorkloadRequest;
import dev.gym.workloadservice.dto.MonthlySummaryResponse;
import dev.gym.workloadservice.dto.TrainingSummary;
import dev.gym.workloadservice.model.Trainer;
import dev.gym.workloadservice.model.Training;
import dev.gym.workloadservice.repository.TrainerRepository;
import dev.gym.workloadservice.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    public void processTrainingChange(TrainerWorkloadRequest trainerWorkloadRequest) {
        Trainer trainer = trainerRepository.findByUsername(trainerWorkloadRequest.trainerUsername())
                .orElse(new Trainer());

        // if the trainer is not found, create a new one
        if (trainer.getUsername() == null) {
            trainer.setUsername(trainerWorkloadRequest.trainerUsername());
            trainer.setFirstName(trainerWorkloadRequest.trainerFirstname());
            trainer.setLastName(trainerWorkloadRequest.trainerLastname());
            trainer.setActive(trainerWorkloadRequest.isActive());
        }

        Training training = new Training();
        training.setTrainingDate(trainerWorkloadRequest.trainingDate());
        training.setTrainingDuration(trainerWorkloadRequest.trainingDuration());
        training.setActionType(trainerWorkloadRequest.actionType());
        training.setTrainer(trainer);

        trainingRepository.save(training);
    }

    public List<MonthlySummaryResponse> calculateMonthlySummary() {
        return trainerRepository.findAll().stream()
                .map(trainer -> new MonthlySummaryResponse(
                        trainer.getUsername(),
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.isActive(),
                        convertToTrainingSummaries(calculateMonthlyDurations(trainer.getTrainings()))
                ))
                .toList();
    }

    protected Map<YearMonth, Integer> calculateMonthlyDurations(List<Training> trainings) {
        return trainings.stream()
                .collect(Collectors.groupingBy(
                        training -> YearMonth.of(training.getTrainingDate().getYear(), training.getTrainingDate().getMonth()),
                        Collectors.summingInt(Training::getTrainingDuration)
                ));
    }

    protected List<TrainingSummary> convertToTrainingSummaries(Map<YearMonth, Integer> monthlyDurations) {
        return monthlyDurations.entrySet().stream()
                .map(entry -> new TrainingSummary(
                        entry.getKey().getYear(),
                        entry.getKey().getMonthValue(),
                        entry.getValue()
                ))
                .toList();
    }
}
