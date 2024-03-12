package dev.gym.workloadservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class YearlySummary {

        int year;
        List<MonthlySummary> months;

        public void addMonthlySummary(MonthlySummary monthlySummary) {
                this.months.add(monthlySummary);
        }
}