package dev.gym.workloadservice.service.converter;

import dev.gym.workloadservice.dto.MonthlyReportProjection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MonthlyReportConverter {

    public static Map<String, Object> convert(List<MonthlyReportProjection> reports) {
        Map<String, Object> result = new HashMap<>();

        if (!reports.isEmpty()) {
            MonthlyReportProjection firstProjection = reports.get(0);
            result.put("Trainer Username", firstProjection.getUsername());
            result.put("Trainer First Name", firstProjection.getFirstName());
            result.put("Trainer Last Name", firstProjection.getLastName());
            result.put("Trainer Status", firstProjection.getIsActive());

            Map<Integer, Map<Integer, Integer>> years = reports.stream()
                    .collect(Collectors.groupingBy(
                            MonthlyReportProjection::getYear,
                            Collectors.groupingBy(
                                    MonthlyReportProjection::getMonth,
                                    Collectors.summingInt(MonthlyReportProjection::getTotalTrainingDuration)
                            )
                    ));

            result.put("Years", years);
        }

        return result;
    }
}
