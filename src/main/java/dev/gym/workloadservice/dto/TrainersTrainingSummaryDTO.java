package dev.gym.workloadservice.dto;

import dev.gym.workloadservice.model.YearlySummary;

import java.util.List;

public record TrainersTrainingSummaryDTO (
        String trainerUsername,
        String trainerFirstname,
        String trainerLastname,
        boolean isTrainerActive,
        List<YearlySummary>years ) { }
