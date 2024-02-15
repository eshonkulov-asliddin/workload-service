package dev.gym.workloadservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSummary {
    private int year;
    private int month;
    private int totalTrainingDuration;
}
