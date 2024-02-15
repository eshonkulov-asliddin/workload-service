package dev.gym.workloadservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WorkloadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkloadServiceApplication.class, args);
    }

}
