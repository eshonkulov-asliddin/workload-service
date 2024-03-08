package dev.gym.workloadservice.rabbitmq;

import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.service.TrainersTrainingSummaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorkloadConsumerTest {

    @Mock
    private TrainersTrainingSummaryService trainersTrainingSummaryService;
    @InjectMocks
    private WorkloadConsumer workloadConsumer;

    @Test
    void whenReceiveMessage_thenProcessTrainingChange() {
        TrainerWorkload trainerWorkload = mock(TrainerWorkload.class);
        String transactionId = UUID.randomUUID().toString();
        workloadConsumer.receiveMessage(transactionId, trainerWorkload);

        verify(trainersTrainingSummaryService, times(1)).processWorkload(trainerWorkload);
    }
}
