package dev.gym.workloadservice.service.converter;

import dev.gym.workloadservice.dto.MonthlyReportDTO;
import dev.gym.workloadservice.dto.TrainerReportDTO;
import dev.gym.workloadservice.dto.TrainerReportProjection;
import dev.gym.workloadservice.dto.YearlyReportDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportConverterTest {

    private static final String username = "username";
    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private static final boolean isActive = true;
    private static final int year = LocalDate.now().getYear();
    private static final int month = LocalDate.now().getMonthValue();
    private static final int totalTrainingDuration = 70;
    private List<TrainerReportProjection> reports;

    @BeforeEach
    void setUp() {
        TrainerReportProjection report = mock(TrainerReportProjection.class);
        when(report.username()).thenReturn(username);
        when(report.firstName()).thenReturn(firstName);
        when(report.lastName()).thenReturn(lastName);
        when(report.isActive()).thenReturn(isActive);
        when(report.year()).thenReturn(year);
        when(report.month()).thenReturn(month);
        when(report.totalTrainingDuration()).thenReturn(totalTrainingDuration);
        reports = new ArrayList<>();
        reports.add(report);
    }


    @Test
    void givenReports_whenConvert_thenReturnTrainerReportDTO() {
        Optional<TrainerReportDTO> result = ReportConverter.convert(reports);
        assertEquals(username, result.get().username());
        assertEquals(firstName, result.get().firstName());
        assertEquals(lastName, result.get().lastName());
        assertEquals(isActive, result.get().isActive());
        assertNotNull(result.get().years());
    }

    @Test
    void givenMonthlyReportProjection_whenConvertToYears_thenReturnYearlyReportDTO() {
        List<YearlyReportDTO> yearlyReportDTOS = ReportConverter.convertToYears(reports);
        assertEquals(year, yearlyReportDTOS.get(0).year());
        assertNotNull(yearlyReportDTOS.get(0).months());
    }

    @Test
    void givenReportProjection_whenConvertToMonth_thenReturnMonthlyReportDTO() {
        List<MonthlyReportDTO> monthlyReportDTOS = ReportConverter.convertToMonths(reports);
        assertEquals(month, monthlyReportDTOS.get(0).month());
        assertEquals(totalTrainingDuration, monthlyReportDTOS.get(0).totalTrainingDuration());
    }
}
