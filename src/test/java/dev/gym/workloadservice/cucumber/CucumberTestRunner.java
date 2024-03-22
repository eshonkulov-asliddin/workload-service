package dev.gym.workloadservice.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import static dev.gym.workloadservice.cucumber.steps.util.Utils.CUCUMBER_FEATURES_PATH;
import static dev.gym.workloadservice.cucumber.steps.util.Utils.CUCUMBER_GLUE_PATH;

@RunWith(Cucumber.class)
@CucumberOptions(features = CUCUMBER_FEATURES_PATH, glue = CUCUMBER_GLUE_PATH)
public class CucumberTestRunner {
}
