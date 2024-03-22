package dev.gym.workloadservice.cucumber.steps;

import dev.gym.workloadservice.cucumber.steps.util.TrainerWorkloadHttpClient;
import dev.gym.workloadservice.dto.TrainerWorkload;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.gym.workloadservice.cucumber.steps.util.Utils.ACTION_TYPE;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.IS_ACTIVE;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINER_FIRSTNAME;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINER_LASTNAME;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINER_USERNAME;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINING_DATE;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.TRAINING_DURATION;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessTrainingChange {

    @Autowired
    private TrainerWorkloadHttpClient httpClient;

    private TrainerWorkload trainerWorkload;
    private int processTrainingChangeStatusCodeAuthorized;
    private int processTrainingChangeStatusCodeUnAuthorized;

    @Given("a valid trainer workload request")
    public void a_valid_trainer_workload_request_authorized() {
        trainerWorkload = createValidTrainerWorkload();
    }

    @When("the authorized client sends the request to process the training change")
    public void the_client_sends_the_request_to_process_the_training_change_authorized() {
        processTrainingChangeStatusCodeAuthorized = httpClient.processTrainingChangeAuthorized(trainerWorkload);
    }

    @Then("the training change should be processed successfully")
    public void training_change_should_be_processed_successfully_authorized() {
        assertEquals(201, processTrainingChangeStatusCodeAuthorized, "Error while processing training change");
    }

    @Given("a trainer workload request")
    public void valid_trainer_workload_request_un_authorized() {
        trainerWorkload = createValidTrainerWorkload();
    }

    @When("the unauthorized client sends the request to process the training change")
    public void the_client_sends_the_request_to_process_the_training_change_unauthorized() {
        processTrainingChangeStatusCodeUnAuthorized = httpClient.processTrainingChangeUnAuthorized(trainerWorkload);
    }

    @Then("the return should be 403")
    public void return_unauthorized() {
        assertEquals(403, processTrainingChangeStatusCodeUnAuthorized, "Error while processing training change");
    }

    private TrainerWorkload createValidTrainerWorkload() {
        return new TrainerWorkload(TRAINER_USERNAME, TRAINER_FIRSTNAME, TRAINER_LASTNAME, IS_ACTIVE, TRAINING_DATE, TRAINING_DURATION, ACTION_TYPE); // valid TrainerWorkload
    }

}
