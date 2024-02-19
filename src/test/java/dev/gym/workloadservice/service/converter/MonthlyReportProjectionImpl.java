package dev.gym.workloadservice.service.converter;

import dev.gym.workloadservice.dto.MonthlyReportProjection;

public class MonthlyReportProjectionImpl implements MonthlyReportProjection {

    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private int year;
    private int month;
    private int totalTrainingDuration;


    public MonthlyReportProjectionImpl(String username, String firstName, String lastName, boolean isActive, int year, int month, int totalTrainingDuration) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.year = year;
        this.month = month;
        this.totalTrainingDuration = totalTrainingDuration;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getMonth() {
        return month;
    }

    @Override
    public int getTotalTrainingDuration() {
        return totalTrainingDuration;
    }
}
