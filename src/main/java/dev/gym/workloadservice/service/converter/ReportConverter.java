package dev.gym.workloadservice.service.converter;

import dev.gym.workloadservice.dto.MonthlyReportDTO;
import dev.gym.workloadservice.dto.TrainerReportDTO;
import dev.gym.workloadservice.dto.TrainerReportProjection;
import dev.gym.workloadservice.dto.YearlyReportDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportConverter {

    public static Optional<TrainerReportDTO> convert(List<TrainerReportProjection> reports) {
        return reports.stream().findFirst()
                .map(report -> new TrainerReportDTO(
                        report.username(),
                        report.firstName(),
                        report.lastName(),
                        report.isActive(),
                        convertToYears(reports)));
    }

    protected static List<YearlyReportDTO> convertToYears(List<TrainerReportProjection> reports) {
        return reports.stream()
                .collect(Collectors.groupingBy(TrainerReportProjection::year))
                .entrySet().stream()
                .map(entry -> new YearlyReportDTO(entry.getKey(), convertToMonths(entry.getValue())))
                .toList();

    }

    protected static List<MonthlyReportDTO> convertToMonths(List<TrainerReportProjection> reports) {
        return reports.stream()
                .map(report -> new MonthlyReportDTO(report.month(), report.totalTrainingDuration()))
                .sorted(Comparator.comparing(MonthlyReportDTO::month))
                .toList();
    }

}
