package dev.gym.workloadservice.cucumber.steps.util;

import dev.gym.workloadservice.model.ActionType;

import java.time.LocalDate;

public class Utils {
    public static final String TRAINER_USERNAME = "testU";
    public static final String TRAINER_FIRSTNAME = "testF";
    public static final String TRAINER_LASTNAME = "testL";
    public static final boolean IS_ACTIVE = true;
    public static final LocalDate TRAINING_DATE = LocalDate.now().plusDays(2);
    public static final int TRAINING_DURATION = 60;
    public static final ActionType ACTION_TYPE = ActionType.ADD;
    public static final String CUCUMBER_FEATURES_PATH = "src/test/resources/features";
    public static final String CUCUMBER_GLUE_PATH = "dev.gym.workloadservice.cucumber";
}
