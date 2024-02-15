package dev.gym.workloadservice.dto;

import dev.gym.workloadservice.model.ActionType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TrainingDto (
        @NotNull(message = "Training date is required")
        LocalDate date,
        @NotNull(message = "Training duration is required")
        int duration,
        @NotNull(message = "Action type is required")
        ActionType actionType){ }
