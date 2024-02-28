package dev.gym.workloadservice.service;

import dev.gym.workloadservice.dto.TrainerWorkload;
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
        TrainerWorkload trainerWorkload = mock(TrainerWorkload.class);
        when(trainerWorkload.trainerUsername()).thenReturn("trainerUsername");
        when(trainerWorkload.trainerFirstname()).thenReturn("trainerFirstname");
        when(trainerWorkload.trainerLastname()).thenReturn("trainerLastname");
        when(trainerWorkload.isActive()).thenReturn(true);
        when(trainerWorkload.trainingDate()).thenReturn(LocalDate.now().plusDays(2));
        when(trainerWorkload.trainingDuration()).thenReturn(60);
        when(trainerWorkload.actionType()).thenReturn(ActionType.ADD);

        // mock Trainer
        Trainer trainer = mock(Trainer.class);

        // mock Training
        Training training = mock(Training.class);
        training.setTrainingDate(trainerWorkload.trainingDate());
        training.setTrainingDuration(trainerWorkload.trainingDuration());
        training.setActionType(trainerWorkload.actionType());
        training.setTrainer(trainer);

        // mock the repository
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(trainer)); // trainer with null fields
        when(trainingRepository.save(ArgumentMatchers.any(Training.class))).thenReturn(training);

        //****************** WHEN *******************//
        trainerWorkloadService.processTrainingChange(trainerWorkload);

        //****************** THEN *******************//
        // verify new trainer is created
        verify(trainer, times(1)).setUsername(trainerWorkload.trainerUsername());
        verify(trainer, times(1)).setFirstName(trainerWorkload.trainerFirstname());
        verify(trainer, times(1)).setLastName(trainerWorkload.trainerLastname());
        verify(trainer, times(1)).setActive(trainerWorkload.isActive());

        // verify training is created
        verify(training, times(1)).setTrainingDate(trainerWorkload.trainingDate());
        verify(training, times(1)).setTrainingDuration(trainerWorkload.trainingDuration());
        verify(training, times(1)).setActionType(trainerWorkload.actionType());
        verify(training, times(1)).setTrainer(trainer);

        // verify the repository is called
        verify(trainerRepository, times(1)).findByUsername(anyString());
        verify(trainingRepository, times(1)).save(ArgumentMatchers.any(Training.class));

    }

    @Test
    void givenValidTrainerWorkloadRequest_whenTrainerExists_thenRetrieveTrainer() {
        //************************ GIVEN ************************//
        //mocking the TrainerWorkloadRequest
        TrainerWorkload trainerWorkload = mock(TrainerWorkload.class);
        when(trainerWorkload.trainerUsername()).thenReturn("trainerUsername");
        when(trainerWorkload.trainerFirstname()).thenReturn("trainerFirstname");
        when(trainerWorkload.trainerLastname()).thenReturn("trainerLastname");
        when(trainerWorkload.isActive()).thenReturn(true);
        when(trainerWorkload.trainingDate()).thenReturn(LocalDate.now().plusDays(2));
        when(trainerWorkload.trainingDuration()).thenReturn(60);
        when(trainerWorkload.actionType()).thenReturn(ActionType.ADD);

        // Trainer object
        Trainer trainer = new Trainer();
        trainer.setUsername(trainerWorkload.trainerUsername());
        trainer.setFirstName(trainerWorkload.trainerFirstname());
        trainer.setLastName(trainerWorkload.trainerLastname());
        trainer.setActive(trainerWorkload.isActive());

        Trainer mockTrainer = spy(trainer);

        // mock Training
        Training training = mock(Training.class);
        training.setTrainingDate(trainerWorkload.trainingDate());
        training.setTrainingDuration(trainerWorkload.trainingDuration());
        training.setActionType(trainerWorkload.actionType());
        training.setTrainer(trainer);

        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(mockTrainer)); // simulate existing trainer
        when(trainingRepository.save(ArgumentMatchers.any(Training.class))).thenReturn(training);

        //************************ WHEN ************************//
        trainerWorkloadService.processTrainingChange(trainerWorkload);

        //*********************** THEN ************************//
        // verify existing trainer is used
        verify(mockTrainer, times(0)).setUsername(trainerWorkload.trainerUsername());
        verify(mockTrainer, times(0)).setFirstName(trainerWorkload.trainerFirstname());
        verify(mockTrainer, times(0)).setLastName(trainerWorkload.trainerLastname());
        verify(mockTrainer, times(0)).setActive(trainerWorkload.isActive());

        // verify new training is created
        verify(training, times(1)).setTrainingDate(trainerWorkload.trainingDate());
        verify(training, times(1)).setTrainingDuration(trainerWorkload.trainingDuration());
        verify(training, times(1)).setActionType(trainerWorkload.actionType());
        verify(training, times(1)).setTrainer(trainer);

        // verify the repository is called
        verify(trainerRepository, times(1)).findByUsername(anyString());
        verify(trainingRepository, times(1)).save(ArgumentMatchers.any(Training.class));

    }

}
