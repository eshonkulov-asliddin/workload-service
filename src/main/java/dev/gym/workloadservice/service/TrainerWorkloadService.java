package dev.gym.workloadservice.service;

import dev.gym.workloadservice.dto.TrainerReportDTO;
import dev.gym.workloadservice.dto.TrainerReportProjection;
import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.model.ActionType;
import dev.gym.workloadservice.model.Trainer;
import dev.gym.workloadservice.model.Training;
import dev.gym.workloadservice.repository.TrainerRepository;
import dev.gym.workloadservice.repository.TrainingRepository;
import dev.gym.workloadservice.service.converter.ReportConverter;
import dev.gym.workloadservice.service.exception.NotFoundException;
import dev.gym.workloadservice.service.exception.util.ExceptionConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadService.class);
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    @Transactional
    public void processTrainingChange(TrainerWorkload trainerWorkload) {
        LOGGER.info("Processing training change for trainer with username: {}", trainerWorkload.trainerUsername());
        Trainer trainer = trainerRepository.findByUsername(trainerWorkload.trainerUsername())
                .orElse(new Trainer());

        // if the trainer is not found, create a new one
        if (trainer.getUsername() == null) {
            LOGGER.info("Trainer with username: {} not found, creating a new one", trainerWorkload.trainerUsername());
            trainer.setUsername(trainerWorkload.trainerUsername());
            trainer.setFirstName(trainerWorkload.trainerFirstname());
            trainer.setLastName(trainerWorkload.trainerLastname());
            trainer.setActive(trainerWorkload.isActive());
        }

        ActionType actionType = trainerWorkload.actionType();
        int trainingDuration = trainerWorkload.trainingDuration();

        LOGGER.info("Creating a new training for trainer with username: {}", trainerWorkload.trainerUsername());
        Training training = new Training();
        training.setTrainingDate(trainerWorkload.trainingDate());
        training.setTrainingDuration(actionType == ActionType.ADD ? trainingDuration : -trainingDuration);
        training.setActionType(trainerWorkload.actionType());
        training.setTrainer(trainer);

        trainingRepository.save(training);
    }

    public Optional<TrainerReportDTO> getMonthlyReportByUsername(String username) {
        LOGGER.info("Calculating monthly report for trainer with username: {}", username);
        if (!trainerRepository.existsByUsername(username)) {
            LOGGER.info("Trainer with username: {} not found", username);
            throw new NotFoundException(String.format(ExceptionConstants.TRAINER_NOT_FOUND, username));
        }
        List<TrainerReportProjection> trainerReportProjections = trainerRepository.calculateMonthlyReportByUsername(username);
        return ReportConverter.convert(trainerReportProjections);
    }

}
