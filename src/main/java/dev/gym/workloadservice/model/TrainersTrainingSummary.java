package dev.gym.workloadservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("trainersTrainingSummary")
public class TrainersTrainingSummary {

    @Id
    private String id;
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private boolean isTrainerActive;
    private List<YearlySummary> years;

    public void addYearlySummary(YearlySummary yearlySummary) {
        this.years.add(yearlySummary);
    }
}
