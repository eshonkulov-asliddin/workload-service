package dev.gym.workloadservice.dto;

public interface TrainerReportProjection {
    String getUsername();
    String getFirstName();
    String getLastName();
    boolean getIsActive();
    int getYear();
    int getMonth();
    int getTotalTrainingDuration();
}
