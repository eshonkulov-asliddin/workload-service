package dev.gym.workloadservice.rabbitmq;

import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.service.TrainersTrainingSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static dev.gym.workloadservice.rabbitmq.RabbitMQConsts.WORKLOAD_MESSAGES;

@Component
@Profile("!test")
public class WorkloadConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(WorkloadConsumer.class);
    @Autowired
    private TrainersTrainingSummaryService trainersTrainingSummaryService;

    @RabbitListener(queues = WORKLOAD_MESSAGES)
    public void receiveMessage(@Header("X-Transaction-ID") String transactionId, TrainerWorkload trainerWorkload) {
        LOGGER.info("Transaction ID <{}> Received < {} >", transactionId, trainerWorkload);
        trainersTrainingSummaryService.processWorkload(trainerWorkload);
    }

}