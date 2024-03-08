package dev.gym.workloadservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySummary {

        private int month;
        private int totalTrainingDuration ;

        public void updateTotalTrainingDuration(int duration) {
                this.totalTrainingDuration += duration;
        }

}
