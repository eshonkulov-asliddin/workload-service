package dev.gym.workloadservice.dto;

public record TrainerReportProjection (
        String username,
        String firstName,
        String lastName,
        boolean isActive,
        int year,
        int month,
        int totalTrainingDuration ) { }
