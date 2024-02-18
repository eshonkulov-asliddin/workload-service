package dev.gym.workloadservice.dto;

public interface MonthlyReportProjection {
    String getUsername();
    String getFirstname();
    String getLastname();
    boolean getIsActive();
    int getYear();
    int getMonth();
    int getTotalTrainingDuration();
}
