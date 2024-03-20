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

    @Value("${serverUrl}")
    private String serverUrl;

    @Value("${jwt.secret.key}")
    private String secret;

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    @LocalServerPort
    private int port;

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public TrainerWorkloadHttpClient(JwtUtil jwtUtil, RestTemplate restTemplate) {
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
    }

    public int processTrainingChangeAuthorized(TrainerWorkload trainerWorkload) {
        String token = generateJwtToken();
        HttpHeaders headers = createAuthorizationHeaders(token);
        HttpEntity<TrainerWorkload> requestEntity = new HttpEntity<>(trainerWorkload, headers);
        return executePostRequest(getProcessTrainingChangeEndpoint(), requestEntity);
    }

    public int processTrainingChangeUnAuthorized(TrainerWorkload trainerWorkload) {
        HttpHeaders headers = createAuthorizationHeaders(null);
        HttpEntity<TrainerWorkload> requestEntity = new HttpEntity<>(trainerWorkload, headers);
        return executePostRequest(getProcessTrainingChangeEndpoint(), requestEntity);
    }

    public ResponseEntity<TrainersTrainingSummaryDTO> getMonthlyReportByTrainerUsernameAuthorized(String username) {
        String token = generateJwtToken();
        HttpHeaders headers = createAuthorizationHeaders(token);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        String url = getMonthlyReportEndpoint() + "/" + username;
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, TrainersTrainingSummaryDTO.class);
    }

    private String generateJwtToken() {
        jwtUtil.setSecret(secret);
        jwtUtil.setExpirationTime(expirationTime);
        return jwtUtil.generateToken(Utils.TRAINER_USERNAME);
    }

    private HttpHeaders createAuthorizationHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    private int executePostRequest(String url, HttpEntity<TrainerWorkload> requestEntity) {
        try {
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class).getStatusCode().value();
        } catch (HttpClientErrorException e) {
            return e.getStatusCode().value();
        }
    }

    private String getMonthlyReportEndpoint() {
        return serverUrl + ":" + port + TRAINER_WORKLOAD_MONTHLY_REPORT_API_ROOT_PATH;
    }

    private String getProcessTrainingChangeEndpoint() {
        return serverUrl + ":" + port + TRAINER_WORKLOAD_API_ROOT_PATH;
    }

}
