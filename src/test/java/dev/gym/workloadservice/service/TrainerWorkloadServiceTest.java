package dev.gym.workloadservice.service;

import dev.gym.workloadservice.dto.TrainerWorkloadRequest;
import dev.gym.workloadservice.model.ActionType;
import dev.gym.workloadservice.model.Trainer;
import dev.gym.workloadservice.model.Training;
import dev.gym.workloadservice.repository.TrainerRepository;
import dev.gym.workloadservice.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceTest {


    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @InjectMocks
    private TrainerWorkloadService trainerWorkloadService;

    @Test
    void givenValidTrainerWorkloadRequest_whenTrainerIsNew_thenCreateNewTrainer() {

        //******************* GIVEN *******************//
        //mocking the TrainerWorkloadRequest
        TrainerWorkloadRequest trainerWorkloadRequest = mock(TrainerWorkloadRequest.class);
        when(trainerWorkloadRequest.trainerUsername()).thenReturn("trainerUsername");
        when(trainerWorkloadRequest.trainerFirstname()).thenReturn("trainerFirstname");
        when(trainerWorkloadRequest.trainerLastname()).thenReturn("trainerLastname");
        when(trainerWorkloadRequest.isActive()).thenReturn(true);
        when(trainerWorkloadRequest.trainingDate()).thenReturn(LocalDate.now().plusDays(2));
        when(trainerWorkloadRequest.trainingDuration()).thenReturn(60);
        when(trainerWorkloadRequest.actionType()).thenReturn(ActionType.ADD);

        // mock Trainer
        Trainer trainer = mock(Trainer.class);

        // mock Training
        Training training = mock(Training.class);
        training.setTrainingDate(trainerWorkloadRequest.trainingDate());
        training.setTrainingDuration(trainerWorkloadRequest.trainingDuration());
        training.setActionType(trainerWorkloadRequest.actionType());
        training.setTrainer(trainer);

        // mock the repository
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(trainer)); // trainer with null fields
        when(trainingRepository.save(ArgumentMatchers.any(Training.class))).thenReturn(training);

        //****************** WHEN *******************//
        trainerWorkloadService.processTrainingChange(trainerWorkloadRequest);

        //****************** THEN *******************//
        // verify new trainer is created
        verify(trainer, times(1)).setUsername(trainerWorkloadRequest.trainerUsername());
        verify(trainer, times(1)).setFirstName(trainerWorkloadRequest.trainerFirstname());
        verify(trainer, times(1)).setLastName(trainerWorkloadRequest.trainerLastname());
        verify(trainer, times(1)).setActive(trainerWorkloadRequest.isActive());

        // verify training is created
        verify(training, times(1)).setTrainingDate(trainerWorkloadRequest.trainingDate());
        verify(training, times(1)).setTrainingDuration(trainerWorkloadRequest.trainingDuration());
        verify(training, times(1)).setActionType(trainerWorkloadRequest.actionType());
        verify(training, times(1)).setTrainer(trainer);

        // verify the repository is called
        verify(trainerRepository, times(1)).findByUsername(anyString());
        verify(trainingRepository, times(1)).save(ArgumentMatchers.any(Training.class));

    }

    @Test
    void givenValidTrainerWorkloadRequest_whenTrainerExists_thenRetrieveTrainer() {
        //************************ GIVEN ************************//
        //mocking the TrainerWorkloadRequest
        TrainerWorkloadRequest trainerWorkloadRequest = mock(TrainerWorkloadRequest.class);
        when(trainerWorkloadRequest.trainerUsername()).thenReturn("trainerUsername");
        when(trainerWorkloadRequest.trainerFirstname()).thenReturn("trainerFirstname");
        when(trainerWorkloadRequest.trainerLastname()).thenReturn("trainerLastname");
        when(trainerWorkloadRequest.isActive()).thenReturn(true);
        when(trainerWorkloadRequest.trainingDate()).thenReturn(LocalDate.now().plusDays(2));
        when(trainerWorkloadRequest.trainingDuration()).thenReturn(60);
        when(trainerWorkloadRequest.actionType()).thenReturn(ActionType.ADD);

        // Trainer object
        Trainer trainer = new Trainer();
        trainer.setUsername(trainerWorkloadRequest.trainerUsername());
        trainer.setFirstName(trainerWorkloadRequest.trainerFirstname());
        trainer.setLastName(trainerWorkloadRequest.trainerLastname());
        trainer.setActive(trainerWorkloadRequest.isActive());

        Trainer mockTrainer = spy(trainer);

        // mock Training
        Training training = mock(Training.class);
        training.setTrainingDate(trainerWorkloadRequest.trainingDate());
        training.setTrainingDuration(trainerWorkloadRequest.trainingDuration());
        training.setActionType(trainerWorkloadRequest.actionType());
        training.setTrainer(trainer);

        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(mockTrainer)); // simulate existing trainer
        when(trainingRepository.save(ArgumentMatchers.any(Training.class))).thenReturn(training);

        //************************ WHEN ************************//
        trainerWorkloadService.processTrainingChange(trainerWorkloadRequest);

        //*********************** THEN ************************//
        // verify existing trainer is used
        verify(mockTrainer, times(0)).setUsername(trainerWorkloadRequest.trainerUsername());
        verify(mockTrainer, times(0)).setFirstName(trainerWorkloadRequest.trainerFirstname());
        verify(mockTrainer, times(0)).setLastName(trainerWorkloadRequest.trainerLastname());
        verify(mockTrainer, times(0)).setActive(trainerWorkloadRequest.isActive());

        // verify new training is created
        verify(training, times(1)).setTrainingDate(trainerWorkloadRequest.trainingDate());
        verify(training, times(1)).setTrainingDuration(trainerWorkloadRequest.trainingDuration());
        verify(training, times(1)).setActionType(trainerWorkloadRequest.actionType());
        verify(training, times(1)).setTrainer(trainer);

        // verify the repository is called
        verify(trainerRepository, times(1)).findByUsername(anyString());
        verify(trainingRepository, times(1)).save(ArgumentMatchers.any(Training.class));

    }

}
