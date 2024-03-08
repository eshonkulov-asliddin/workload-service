package dev.gym.workloadservice.service;

import dev.gym.workloadservice.converter.TrainersTrainingSummaryToDtoConverter;
import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.dto.TrainersTrainingSummaryDTO;
import dev.gym.workloadservice.model.ActionType;
import dev.gym.workloadservice.model.MonthlySummary;
import dev.gym.workloadservice.model.TrainersTrainingSummary;
import dev.gym.workloadservice.model.YearlySummary;
import dev.gym.workloadservice.repository.TrainersTrainingSummaryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainersTrainingSummaryService {

    private final TrainersTrainingSummaryRepo trainersTrainingSummaryRepo;
    private final TrainersTrainingSummaryToDtoConverter converter;

    public Optional<TrainersTrainingSummaryDTO> getTrainingSummaryByUsername(String username) {
        Optional<TrainersTrainingSummary> trainersTrainingSummary = trainersTrainingSummaryRepo.findSummaryByTrainerUsername(username);
        return trainersTrainingSummary.map(converter::convert);
    }

    public void processWorkload(TrainerWorkload trainerWorkload) {
        String username = trainerWorkload.trainerUsername();
        Optional<TrainersTrainingSummary> trainersTrainingSummary = trainersTrainingSummaryRepo.findSummaryByTrainerUsername(username);

        trainersTrainingSummary.ifPresentOrElse(summary -> updateOrAddSummary(trainerWorkload, summary),
                () -> createSummary(trainerWorkload));
    }

    protected void updateOrAddSummary(TrainerWorkload trainerWorkload, TrainersTrainingSummary trainersTrainingSummary) {
        int trainingDuration = calculateTrainingDuration(trainerWorkload);
        int month = trainerWorkload.trainingDate().getMonthValue();
        int year = trainerWorkload.trainingDate().getYear();

        Optional<YearlySummary> existingYearlySummary = findYearlySummary(trainersTrainingSummary, year);

        if (existingYearlySummary.isPresent()) {
            Optional<MonthlySummary> existingMonthlySummary = findMonthlySummary(existingYearlySummary.get(), month);

            if (existingMonthlySummary.isPresent()) {
                existingMonthlySummary.get().updateTotalTrainingDuration(trainingDuration);
            } else {
                existingYearlySummary.get().addMonthlySummary(createMonthlySummary(month, trainingDuration));
            }
        } else {
            trainersTrainingSummary.addYearlySummary(createYearlySummary(year, createMonthlySummary(month, trainingDuration)));
        }

        trainersTrainingSummaryRepo.save(trainersTrainingSummary);
    }

    protected void createSummary(TrainerWorkload trainerWorkload) {
        int trainingDuration = calculateTrainingDuration(trainerWorkload);
        int month = trainerWorkload.trainingDate().getMonthValue();
        int year = trainerWorkload.trainingDate().getYear();

        MonthlySummary monthlySummary = createMonthlySummary(month, trainingDuration);
        YearlySummary yearlySummary = createYearlySummary(year, monthlySummary);

        TrainersTrainingSummary trainingSummary = new TrainersTrainingSummary();
        trainingSummary.setTrainerUsername(trainerWorkload.trainerUsername());
        trainingSummary.setTrainerFirstname(trainerWorkload.trainerFirstname());
        trainingSummary.setTrainerLastname(trainerWorkload.trainerLastname());
        trainingSummary.setTrainerActive(trainerWorkload.isActive());
        trainingSummary.setYears(List.of(yearlySummary));

        trainersTrainingSummaryRepo.save(trainingSummary);
    }

    protected Optional<YearlySummary> findYearlySummary(TrainersTrainingSummary trainersTrainingSummary, int year) {
        return trainersTrainingSummary.getYears().stream()
                .filter(yearly -> yearly.getYear() == year)
                .findFirst();
    }

    protected Optional<MonthlySummary> findMonthlySummary(YearlySummary yearlySummary, int month) {
        return yearlySummary.getMonths().stream()
                .filter(monthly -> monthly.getMonth() == month)
                .findFirst();
    }

    private MonthlySummary createMonthlySummary(int month, int trainingDuration) {
        return new MonthlySummary(month, trainingDuration);
    }

    private YearlySummary createYearlySummary(int year, MonthlySummary monthlySummary) {
        return new YearlySummary(year, List.of(monthlySummary));
    }

    private int calculateTrainingDuration(TrainerWorkload trainerWorkload) {
        int duration = trainerWorkload.trainingDuration();
        return trainerWorkload.actionType() == ActionType.ADD ? duration : -duration;
    }

}
