package dev.gym.workloadservice.dto;

import java.util.List;

public record TrainerReportDTO (
        String username,
        String firstName,
        String lastName,
        boolean isActive,
        List<YearlyReportDTO> years ) { }
