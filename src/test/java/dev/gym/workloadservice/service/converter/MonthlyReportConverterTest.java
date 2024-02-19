package dev.gym.workloadservice.service.converter;

import dev.gym.workloadservice.dto.MonthlyReportProjection;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlyReportConverterTest {

    @Test
    void testConvert() {
        // Create a list of MonthlyReportProjection objects
        List<MonthlyReportProjection> reports = Arrays.asList(
                new MonthlyReportProjectionImpl("username", "John", "Doe", true, 2022, 1, 10),
                new MonthlyReportProjectionImpl("username", "John", "Doe", true, 2022, 2, 15),
                new MonthlyReportProjectionImpl("username", "John", "Doe", true, 2023, 1, 20),
                new MonthlyReportProjectionImpl("username", "John", "Doe", true, 2023, 2, 25)
        );

        // Call the convert method
        Map<String, Object> result = MonthlyReportConverter.convert(reports);

        // Assert that the result is as expected
        assertEquals("username", result.get("Trainer Username"));
        assertEquals("John", result.get("Trainer First Name"));
        assertEquals("Doe", result.get("Trainer Last Name"));
        assertEquals(true, result.get("Trainer Status"));

        Map<Integer, Map<Integer, Integer>> years = (Map<Integer, Map<Integer, Integer>>) result.get("Years");
        assertEquals(2, years.size());

        Map<Integer, Integer> year2022 = years.get(2022);
        assertEquals(2, year2022.size());
        assertEquals(10, year2022.get(1));
        assertEquals(15, year2022.get(2));

        Map<Integer, Integer> year2023 = years.get(2023);
        assertEquals(2, year2023.size());
        assertEquals(20, year2023.get(1));
        assertEquals(25, year2023.get(2));
    }

    @Test
    void testConvertEmptyList() {
        // Create an empty list of MonthlyReportProjection objects
        List<MonthlyReportProjection> reports = Arrays.asList();

        // Call the convert method
        Map<String, Object> result = MonthlyReportConverter.convert(reports);

        // Assert that the result is an empty map
        assertEquals(new HashMap<>(), result);
    }
}
