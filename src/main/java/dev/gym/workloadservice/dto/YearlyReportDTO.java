package dev.gym.workloadservice.dto;

import java.util.List;

public record YearlyReportDTO (
    int year,
    List<MonthlyReportDTO> months) { }
