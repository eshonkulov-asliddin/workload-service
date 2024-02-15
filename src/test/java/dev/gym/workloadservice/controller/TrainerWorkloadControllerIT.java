package dev.gym.workloadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gym.workloadservice.dto.TrainerWorkloadRequest;
import dev.gym.workloadservice.jwt.JwtUtil;
import dev.gym.workloadservice.model.ActionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TrainerWorkloadControllerIT {

    @Value("${jwt.secret.key}")
    private String secret;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenRequest_whenNoAuthorizationHeader_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/workload-service/trainers/workload/monthly"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenRequest_whenValidAuthorizationHeader_thenOk() throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        jwtUtil.setSecret(secret);
        String token = jwtUtil.generateToken("testUser");

        mockMvc.perform(get("/workload-service/trainers/workload/monthly")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidRequest_whenCreateWorkload_thenCreated() throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        jwtUtil.setSecret(secret);
        String token = jwtUtil.generateToken("testUser");

        TrainerWorkloadRequest trainerWorkloadRequest = new TrainerWorkloadRequest("testUser", "testFirstname", "testLastName", true, LocalDate.now().plusDays(3), 1, ActionType.ADD);

        mockMvc.perform(post("/workload-service/trainers/workload")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerWorkloadRequest)))
                .andExpect(status().isCreated());
    }

}
