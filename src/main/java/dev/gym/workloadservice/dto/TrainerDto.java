package dev.gym.workloadservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TrainerDto (
        @NotEmpty(message = "Trainer username is required")
        String username,
        @NotEmpty(message = "Trainer firstname is required")
        String firstName,
        @NotEmpty(message = "Trainer lastname is required")
        String lastName,
        @NotNull(message = "Trainer is_active is required")
        boolean isActive) { }
