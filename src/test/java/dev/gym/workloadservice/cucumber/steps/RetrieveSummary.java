package dev.gym.workloadservice.cucumber.steps;

import dev.gym.workloadservice.cucumber.steps.util.TrainerWorkloadHttpClient;
import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.dto.TrainersTrainingSummaryDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static dev.gym.workloadservice.cucumber.steps.util.Utils.ACTION_TYPE;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.IS_ACTIVE;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINER_FIRSTNAME;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINER_LASTNAME;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINER_USERNAME;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINING_DATE;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINING_DURATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RetrieveSummary {

    @Autowired
    private TrainerWorkloadHttpClient httpClient;
    private String username;
    private ResponseEntity<TrainersTrainingSummaryDTO> trainerSummaryByUsername;


    @Given("a valid trainer username")
    public void a_trainer_with_username() {
        username = TRAINER_USERNAME;
        // create training for trainer
        createTrainingForTrainer();
    }

    @When("the authorized client requests the monthly report for the trainer")
    public void the_client_requests_the_monthly_report_for_the_trainer() {
        trainerSummaryByUsername = httpClient.getMonthlyReportByTrainerUsernameAuthorized(username);
    }

    @Then("the response should contain the trainer's training summary")
    public void the_response_should_contain_the_trainer_s_training_summary() {
        assertNotNull(trainerSummaryByUsername);
        assertNotNull(trainerSummaryByUsername.getBody());
        assertEquals(200, trainerSummaryByUsername.getStatusCode().value());
    }

    private void createTrainingForTrainer() {
        TrainerWorkload trainerWorkload = new TrainerWorkload(TRAINER_USERNAME, TRAINER_FIRSTNAME, TRAINER_LASTNAME, IS_ACTIVE, TRAINING_DATE, TRAINING_DURATION, ACTION_TYPE); // valid TrainerWorkload
        httpClient.processTrainingChangeAuthorized(trainerWorkload);
    }
}
