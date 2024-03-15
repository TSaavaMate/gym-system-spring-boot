package com.example.jwtdemo.services.training;

import com.example.jwtdemo.models.dto.TraineeTrainingDto;
import com.example.jwtdemo.models.dto.TrainerTrainingDto;
import com.example.jwtdemo.models.requests.registrationRequest.TrainingRegistration;
import com.example.jwtdemo.models.requests.trainingFilterRequest.TraineeTraining;
import com.example.jwtdemo.models.requests.trainingFilterRequest.TrainerTraining;

import java.util.List;

public interface TrainingService{
    List<TraineeTrainingDto> getTraineeTrainings(TraineeTraining request);
    List<TrainerTrainingDto>  getTrainerTrainings(TrainerTraining request);

    void createTraining(TrainingRegistration request);
}
