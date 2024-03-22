package dev.gym.workloadservice.cucumber.config;

import dev.gym.workloadservice.security.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
