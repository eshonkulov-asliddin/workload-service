package dev.gym.workloadservice.service;

import dev.gym.workloadservice.dto.MonthlyReportProjection;
import dev.gym.workloadservice.dto.TrainerWorkloadRequest;
import dev.gym.workloadservice.model.ActionType;
import dev.gym.workloadservice.model.Trainer;
import dev.gym.workloadservice.model.Training;
import dev.gym.workloadservice.repository.TrainerRepository;
import dev.gym.workloadservice.repository.TrainingRepository;
import dev.gym.workloadservice.service.converter.MonthlyReportConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

        ActionType actionType = trainerWorkloadRequest.actionType();
        int trainingDuration = trainerWorkloadRequest.trainingDuration();

        Training training = new Training();
        training.setTrainingDate(trainerWorkloadRequest.trainingDate());
        training.setTrainingDuration(actionType == ActionType.ADD ? trainingDuration : -trainingDuration);
        training.setActionType(trainerWorkloadRequest.actionType());
        training.setTrainer(trainer);

        trainingRepository.save(training);
    }

    public Map<String, Object> getMonthlyReportByUsername(String username) {
        List<MonthlyReportProjection> monthlyReportProjections = trainerRepository.calculateMonthlyReportByUsername(username);
        return MonthlyReportConverter.convert(monthlyReportProjections);
    }

}
