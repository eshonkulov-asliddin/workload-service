package dev.gym.workloadservice.repository;

import dev.gym.workloadservice.model.TrainersTrainingSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = {MongoDBTestContainerConfig.class})
class TrainersTrainingSummaryRepositoryIT {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TrainersTrainingSummaryRepo trainersTrainingSummaryRepo;

    @Test
    void giveTrainerExists_whenFindByUsername_thenGetUser() {
        TrainersTrainingSummary trainersTrainingSummary = new TrainersTrainingSummary();
        trainersTrainingSummary.setTrainerUsername("testU");
        trainersTrainingSummary.setTrainerFirstname("testF");
        trainersTrainingSummary.setTrainerLastname("testL");
        trainersTrainingSummary.setTrainerActive(true);
        trainersTrainingSummary.setYears(new ArrayList<>());

        mongoTemplate.save(trainersTrainingSummary);

        Optional<TrainersTrainingSummary> summaryByTrainerUsername = trainersTrainingSummaryRepo.findSummaryByTrainerUsername(trainersTrainingSummary.getTrainerUsername());

        assertTrue(summaryByTrainerUsername.isPresent());
        assertEquals(summaryByTrainerUsername.get().getTrainerUsername(), trainersTrainingSummary.getTrainerUsername());
        assertEquals(summaryByTrainerUsername.get().getTrainerFirstname(), trainersTrainingSummary.getTrainerFirstname());

    }

}
