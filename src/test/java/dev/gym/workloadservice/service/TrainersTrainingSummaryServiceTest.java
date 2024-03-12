package dev.gym.workloadservice.service;

import dev.gym.workloadservice.converter.TrainersTrainingSummaryToDtoConverter;
import dev.gym.workloadservice.dto.TrainerWorkload;
import dev.gym.workloadservice.dto.TrainersTrainingSummaryDTO;
import dev.gym.workloadservice.model.ActionType;
import dev.gym.workloadservice.model.MonthlySummary;
import dev.gym.workloadservice.model.TrainersTrainingSummary;
import dev.gym.workloadservice.model.YearlySummary;
import dev.gym.workloadservice.repository.TrainersTrainingSummaryRepo;
import dev.gym.workloadservice.service.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainersTrainingSummaryServiceTest {

    private static final String USERNAME = "testU";
    private static final String FIRSTNAME = "testF";
    private static final String LASTNAME = "testL";
    private static final boolean IS_ACTIVE = true;
    private static final int TRAINING_DURATION = 60;
    private static final LocalDate TRAINING_DATE = LocalDate.now().plusDays(2);
    private static final ActionType ACTION_TYPE = ActionType.ADD;

    @Mock
    private TrainersTrainingSummaryRepo trainersTrainingSummaryRepo;
    @Mock
    private TrainersTrainingSummaryToDtoConverter converter;

    @InjectMocks
    private TrainersTrainingSummaryService trainersTrainingSummaryService;

    @Test
    void givenValidTrainerUsername_whenGetSummary_thenReturnSuccess() {
        // GIVEN
        TrainersTrainingSummary trainersTrainingSummaryMock = mock(TrainersTrainingSummary.class);
        TrainersTrainingSummaryDTO trainersTrainingSummaryDTOMock = mock(TrainersTrainingSummaryDTO.class);

        when(trainersTrainingSummaryDTOMock.trainerUsername()).thenReturn(USERNAME);
        when(trainersTrainingSummaryRepo.findSummaryByTrainerUsername(USERNAME)).thenReturn(Optional.of(trainersTrainingSummaryMock));
        when(converter.convert(any(TrainersTrainingSummary.class))).thenReturn(trainersTrainingSummaryDTOMock);

        // WHEN
        Optional<TrainersTrainingSummaryDTO> trainingSummaryByUsername = trainersTrainingSummaryService.getTrainingSummaryByUsername(USERNAME);

        // THEN
        assertNotNull(trainingSummaryByUsername.get());
        assertEquals(USERNAME, trainingSummaryByUsername.get().trainerUsername());
    }

    @Test
    void givenInvalidTrainerUsername_whenGetSummary_thenThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class,
                () -> trainersTrainingSummaryService.getTrainingSummaryByUsername(USERNAME));
    }

    @Test
    void giveWorkload_whenTrainerDoesNotExist_thenCreate() {
        // GIVEN
        TrainerWorkload trainerWorkloadMock = mock(TrainerWorkload.class);
        when(trainerWorkloadMock.trainerUsername()).thenReturn(USERNAME);
        when(trainerWorkloadMock.trainerFirstname()).thenReturn(FIRSTNAME);
        when(trainerWorkloadMock.trainerLastname()).thenReturn(LASTNAME);
        when(trainerWorkloadMock.isActive()).thenReturn(IS_ACTIVE);
        when(trainerWorkloadMock.trainingDuration()).thenReturn(TRAINING_DURATION);
        when(trainerWorkloadMock.trainingDate()).thenReturn(TRAINING_DATE);
        when(trainersTrainingSummaryRepo.findSummaryByTrainerUsername(USERNAME)).thenReturn(Optional.empty());

        // WHEN
        trainersTrainingSummaryService.processWorkload(trainerWorkloadMock);

        // THEN
        verify(trainersTrainingSummaryRepo, times(1)).save(any(TrainersTrainingSummary.class));
    }

    @Test
    void giveWorkload_whenTrainerExists_thenUpdate() {
        // GIVEN
        TrainerWorkload trainerWorkloadMock = mock(TrainerWorkload.class);
        when(trainerWorkloadMock.trainerUsername()).thenReturn(USERNAME);
        when(trainerWorkloadMock.actionType()).thenReturn(ActionType.ADD);
        when(trainerWorkloadMock.trainingDuration()).thenReturn(60);
        when(trainerWorkloadMock.trainingDate()).thenReturn(LocalDate.now().plusDays(2));

        TrainersTrainingSummary trainersTrainingSummaryMock = mock(TrainersTrainingSummary.class);
        when(trainersTrainingSummaryMock.getYears()).thenReturn(List.of());
        doNothing().when(trainersTrainingSummaryMock).addYearlySummary(any(YearlySummary.class));

        when(trainersTrainingSummaryRepo.findSummaryByTrainerUsername(USERNAME)).thenReturn(Optional.of(trainersTrainingSummaryMock));

        // WHEN
        trainersTrainingSummaryService.processWorkload(trainerWorkloadMock);

        // THEN
        verify(trainersTrainingSummaryRepo, times(1)).save(trainersTrainingSummaryMock);
    }

    @Test
    void givenTrainerWorkload_whenYearlySummaryAndMonthlySummaryExist_thenIncrementTotalTrainingDuration() {
        // GIVEN
        TrainerWorkload trainerWorkloadMock = new TrainerWorkload(USERNAME, FIRSTNAME, LASTNAME, IS_ACTIVE, TRAINING_DATE, TRAINING_DURATION, ACTION_TYPE);
        MonthlySummary monthlySummary = new MonthlySummary(TRAINING_DATE.getMonthValue(), TRAINING_DURATION);
        YearlySummary yearlySummary = new YearlySummary(TRAINING_DATE.getYear(), List.of(monthlySummary));

        TrainersTrainingSummary trainersTrainingSummary = new TrainersTrainingSummary();
        trainersTrainingSummary.setTrainerUsername(USERNAME);
        trainersTrainingSummary.setTrainerFirstname(FIRSTNAME);
        trainersTrainingSummary.setTrainerLastname(LASTNAME);
        trainersTrainingSummary.setTrainerActive(IS_ACTIVE);
        trainersTrainingSummary.setYears(List.of(yearlySummary));

        // WHEN
        trainersTrainingSummaryService.updateOrAddSummary(trainerWorkloadMock, trainersTrainingSummary);

        // THEN
        assertEquals(monthlySummary.getTotalTrainingDuration(), TRAINING_DURATION*2, "Error while incrementing Total Training Duration within month");
        assertEquals(1, yearlySummary.getMonths().size(), "Error while calculating number of months within a yearly summary");
    }

    @Test
    void givenTrainerWorkload_whenYearlySummaryExistsButNotMonthlySummary_thenCreateNewMonthlySummary() {
        // GIVEN
        TrainerWorkload trainerWorkloadMock = new TrainerWorkload(USERNAME, FIRSTNAME, LASTNAME, IS_ACTIVE, TRAINING_DATE, TRAINING_DURATION, ACTION_TYPE);
        YearlySummary yearlySummary = new YearlySummary(TRAINING_DATE.getYear(), new ArrayList<>());

        TrainersTrainingSummary trainersTrainingSummary = new TrainersTrainingSummary();
        trainersTrainingSummary.setTrainerUsername(USERNAME);
        trainersTrainingSummary.setTrainerFirstname(FIRSTNAME);
        trainersTrainingSummary.setTrainerLastname(LASTNAME);
        trainersTrainingSummary.setTrainerActive(IS_ACTIVE);
        trainersTrainingSummary.setYears(List.of(yearlySummary));

        // WHEN
        trainersTrainingSummaryService.updateOrAddSummary(trainerWorkloadMock, trainersTrainingSummary);

        // THEN
        assertEquals(TRAINING_DURATION, yearlySummary.getMonths().get(0).getTotalTrainingDuration(), "Error while creating a monthly summary");
        assertEquals(1, yearlySummary.getMonths().size(), "Error while calculating number of months within a yearly summary");
    }

    @Test
    void givenTrainerWorkload_whenYearlySummaryDoesNotExists_thenCreateNewYearlySummary() {
        // GIVEN
        TrainerWorkload trainerWorkloadMock = new TrainerWorkload(USERNAME, FIRSTNAME, LASTNAME, IS_ACTIVE, TRAINING_DATE, TRAINING_DURATION, ACTION_TYPE);

        TrainersTrainingSummary trainersTrainingSummary = new TrainersTrainingSummary();
        trainersTrainingSummary.setTrainerUsername(USERNAME);
        trainersTrainingSummary.setTrainerFirstname(FIRSTNAME);
        trainersTrainingSummary.setTrainerLastname(LASTNAME);
        trainersTrainingSummary.setTrainerActive(IS_ACTIVE);
        trainersTrainingSummary.setYears(new ArrayList<>());

        // WHEN
        trainersTrainingSummaryService.updateOrAddSummary(trainerWorkloadMock, trainersTrainingSummary);

        // THEN
        assertEquals(1, trainersTrainingSummary.getYears().size(),"Error while creating new YearlySummary");
        assertEquals(TRAINING_DATE.getYear(), trainersTrainingSummary.getYears().get(0).getYear(), "Created YearlySummary with different year");
        assertEquals(1, trainersTrainingSummary.getYears().get(0).getMonths().size(), "Error while calculating number of month within YearlySummary");
    }

    @Test
    void testCalculateTrainingDuration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method calculateTrainingDurationMethod = getCalculateTrainingDurationMethod();
        // ACTION TYPE IS ADD
        TrainerWorkload trainerWorkload = new TrainerWorkload(USERNAME, FIRSTNAME, LASTNAME, IS_ACTIVE, TRAINING_DATE, TRAINING_DURATION, ACTION_TYPE);
        int result = (int) calculateTrainingDurationMethod.invoke(trainersTrainingSummaryService, trainerWorkload);
        assertEquals(TRAINING_DURATION, result);

        // ACTION TYPE IS DELETE
        TrainerWorkload trainerWorkload2 = new TrainerWorkload(USERNAME, FIRSTNAME, LASTNAME, IS_ACTIVE, TRAINING_DATE, TRAINING_DURATION, ActionType.DELETE);
        int result2 = (int) calculateTrainingDurationMethod.invoke(trainersTrainingSummaryService, trainerWorkload2);
        assertEquals(-TRAINING_DURATION, result2);
    }

    private Method getCalculateTrainingDurationMethod() throws NoSuchMethodException {
        Method method = TrainersTrainingSummaryService.class.getDeclaredMethod("calculateTrainingDuration", TrainerWorkload.class);
        method.setAccessible(true);
        return method;
    }
}
