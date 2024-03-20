package dev.gym.workloadservice.cucumber.steps.util;

import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.dto.TrainersTrainingSummaryDTO;
import dev.gym.workloadservice.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static dev.gym.workloadservice.controller.util.RestApiConst.TRAINER_WORKLOAD_API_ROOT_PATH;
import static dev.gym.workloadservice.controller.util.RestApiConst.TRAINER_WORKLOAD_MONTHLY_REPORT_API_ROOT_PATH;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class TrainerWorkloadHttpClient {

    private final String SERVER_URL;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @Value("${jwt.secret.key}")
    private String secret;
    @Value("${jwt.expiration.time}")
    private long expirationTime;

    {
        SERVER_URL =  "http://localhost";
        jwtUtil = new JwtUtil();
        restTemplate = new RestTemplate();
    }

    public int processTrainingChangeAuthorized(final TrainerWorkload trainerWorkload) {
        String token = generateJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<TrainerWorkload> requestEntity = new HttpEntity<>(trainerWorkload, headers);
        return restTemplate.exchange(getProcessTrainingChangeEndpoint(), HttpMethod.POST, requestEntity, Void.class).getStatusCode().value();
    }

    public int processTrainingChangeUnAuthorized(final TrainerWorkload trainerWorkload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TrainerWorkload> requestEntity = new HttpEntity<>(trainerWorkload, headers);
        try {
            return restTemplate.exchange(getProcessTrainingChangeEndpoint(), HttpMethod.POST, requestEntity, Void.class).getStatusCode().value();
        } catch (HttpClientErrorException e) {
            return e.getStatusCode().value();
        }
    }

    public ResponseEntity<TrainersTrainingSummaryDTO> getMonthlyReportByTrainerUsernameAuthorized(String username) {
        String token = generateJwtToken();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        headers.setBearerAuth(token);
        String url = getMonthlyReportEndpoint() + "/" + username;
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, TrainersTrainingSummaryDTO.class);
    }

    private String generateJwtToken() {
        jwtUtil.setSecret(secret);
        jwtUtil.setExpirationTime(expirationTime);
        return jwtUtil.generateToken(Utils.TRAINER_USERNAME);
    }

    private String getMonthlyReportEndpoint() {
        return SERVER_URL + ":" + port + TRAINER_WORKLOAD_MONTHLY_REPORT_API_ROOT_PATH;
    }

    private String getProcessTrainingChangeEndpoint() {
        return SERVER_URL + ":" + port + TRAINER_WORKLOAD_API_ROOT_PATH;
    }

}
