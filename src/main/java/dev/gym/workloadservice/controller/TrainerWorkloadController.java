package dev.gym.workloadservice.controller;

import dev.gym.workloadservice.controller.util.RestApiConst;
import dev.gym.workloadservice.dto.TrainerReportDTO;
import dev.gym.workloadservice.dto.TrainerWorkloadRequest;
import dev.gym.workloadservice.service.TrainerWorkloadService;
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

    private final TrainerWorkloadService trainerWorkloadService;

    @GetMapping(RestApiConst.TRAINER_WORKLOAD_MONTHLY_REPORT_API_ROOT_PATH + "/{username}")
    public Optional<TrainerReportDTO> calculateMonthlyReport(@PathVariable(value = "username") String username) {
        return trainerWorkloadService.getMonthlyReportByUsername(username);
    }

    @PostMapping(RestApiConst.TRAINER_WORKLOAD_API_ROOT_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public void processTrainingChange(@RequestBody @Validated TrainerWorkloadRequest trainerWorkloadRequest) {
        trainerWorkloadService.processTrainingChange(trainerWorkloadRequest);
    }

}
