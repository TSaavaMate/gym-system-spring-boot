package com.example.jwtdemo.services.training;

import com.example.jwtdemo.aspect.Loggable;
import com.example.jwtdemo.entities.Training;
import com.example.jwtdemo.exceptions.InvalidTrainingCreationRequest;
import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.models.dto.TraineeTrainingDto;
import com.example.jwtdemo.models.dto.TrainerTrainingDto;
import com.example.jwtdemo.models.requests.registrationRequest.TrainingRegistration;
import com.example.jwtdemo.models.requests.trainingFilterRequest.TraineeTraining;
import com.example.jwtdemo.models.requests.trainingFilterRequest.TrainerTraining;
import com.example.jwtdemo.repositories.TraineeRepository;
import com.example.jwtdemo.repositories.TrainerRepository;
import com.example.jwtdemo.repositories.TrainingRepository;
import com.example.jwtdemo.services.training.mapper.TraineeTrainingDtoMapper;
import com.example.jwtdemo.services.training.mapper.TrainerTrainingDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcreteTrainingService implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    private final TraineeTrainingDtoMapper traineeTrainingMapper;

    private final TrainerTrainingDtoMapper trainerTrainingDtoMapper;
    @Override
    public List<TraineeTrainingDto> getTraineeTrainings(TraineeTraining request) {

        var trainings = trainingRepository.findTraineeTraining(
                request.getUsername(),
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTrainerFirstName(),
                request.getTrainingTypeName()
        );

        return trainings.stream()
                .map(traineeTrainingMapper)
                .toList();
    }

    @Override
    public List<TrainerTrainingDto> getTrainerTrainings(TrainerTraining request) {
        var trainings = trainingRepository.findTrainerTraining(
                request.getUsername(),
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTraineeFirstName());

        return trainings.stream()
                .map(trainerTrainingDtoMapper)
                .toList();

    }

    @Override
    @Transactional
    @Loggable
    public void createTraining(TrainingRegistration request) {
        var trainee = traineeRepository.findTraineeByUserUsername(request.getTraineeUsername())
                .orElseThrow(() -> new ResourceNotFoundException("not found trainee : cannot create training without trainee"));

        var trainer = trainerRepository.findTrainerByUserUsername(request.getTrainerUsername())
                .orElseThrow(() -> new ResourceNotFoundException("not found trainer : cannot create training without trainer"));

        if (trainee.getUser().getUsername().equals(trainer.getUser().getUsername())){
            log.warn("trainee and trainer could not be same person with username of {}",trainee.getUser().getUsername());
            throw new InvalidTrainingCreationRequest("trainee and trainer could not be same person");
        }
        var training = new Training();


        training.setName(request.getName());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setDate(request.getTrainingDate());
        training.setDuration(request.getDuration());

        trainingRepository.save(training);
    }
}
