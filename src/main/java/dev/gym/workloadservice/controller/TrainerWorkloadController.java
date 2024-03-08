package dev.gym.workloadservice.controller;

import dev.gym.workloadservice.controller.util.RestApiConst;
import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.dto.TrainersTrainingSummaryDTO;
import dev.gym.workloadservice.service.TrainersTrainingSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TrainerWorkloadController {

    private final TrainersTrainingSummaryService trainersTrainingSummaryService;

    @GetMapping(RestApiConst.TRAINER_WORKLOAD_MONTHLY_REPORT_API_ROOT_PATH + "/{username}")
    public Optional<TrainersTrainingSummaryDTO> calculateMonthlyReport(@PathVariable(value = "username") String username) {
        return trainersTrainingSummaryService.getTrainingSummaryByUsername(username);
    }

    @PostMapping(RestApiConst.TRAINER_WORKLOAD_API_ROOT_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public void processTrainingChange(@RequestBody @Validated TrainerWorkload trainerWorkload) {
        trainersTrainingSummaryService.processWorkload(trainerWorkload);
    }

}
