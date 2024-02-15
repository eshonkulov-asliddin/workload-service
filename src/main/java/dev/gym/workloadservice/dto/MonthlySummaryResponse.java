package dev.gym.workloadservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummaryResponse {
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private boolean trainerIsActive;
    private List<TrainingSummary> trainingSummaryList;
}
