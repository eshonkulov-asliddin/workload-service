package dev.gym.workloadservice.converter;

import dev.gym.workloadservice.dto.TrainersTrainingSummaryDTO;
import dev.gym.workloadservice.model.TrainersTrainingSummary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainersTrainingSummaryToDtoConverter implements Converter<TrainersTrainingSummary, TrainersTrainingSummaryDTO> {

    @Override
    public TrainersTrainingSummaryDTO convert(TrainersTrainingSummary source) {
        return new TrainersTrainingSummaryDTO(
                source.getTrainerUsername(),
                source.getTrainerFirstname(),
                source.getTrainerLastname(),
                source.isTrainerActive(),
                source.getYears());
    }
}
