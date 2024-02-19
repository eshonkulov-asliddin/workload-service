package dev.gym.workloadservice.controller;

import dev.gym.workloadservice.controller.util.RestApiConst;
import dev.gym.workloadservice.dto.TrainerWorkloadRequest;
import dev.gym.workloadservice.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = RestApiConst.TRAINER_WORKLOAD_API_ROOT_PATH)
@RequiredArgsConstructor
public class TrainerWorkloadController {

    private final TrainerWorkloadService trainerWorkloadService;

    @GetMapping("/{username}/monthly-reports")
    public Map<String, Object> calculateMonthlyReport(@PathVariable(value = "username") String username) {
        return trainerWorkloadService.getMonthlyReportByUsername(username);
    }

    @PostMapping("/training-changes")
    @ResponseStatus(HttpStatus.CREATED)
    public void processTrainingChange(@RequestBody @Validated TrainerWorkloadRequest trainerWorkloadRequest) {
        trainerWorkloadService.processTrainingChange(trainerWorkloadRequest);
    }

}
