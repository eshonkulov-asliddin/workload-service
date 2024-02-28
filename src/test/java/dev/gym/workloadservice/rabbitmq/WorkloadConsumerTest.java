package dev.gym.workloadservice.rabbitmq;

import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.service.TrainerWorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorkloadConsumerTest {

    @Mock
    private TrainerWorkloadService trainerWorkloadService;
    @InjectMocks
    private WorkloadConsumer workloadConsumer;

    @Test
    void whenReceiveMessage_thenProcessTrainingChange() {
        TrainerWorkload trainerWorkload = mock(TrainerWorkload.class);

        workloadConsumer.receiveMessage(trainerWorkload);

        verify(trainerWorkloadService, times(1)).processTrainingChange(trainerWorkload);
    }
}
