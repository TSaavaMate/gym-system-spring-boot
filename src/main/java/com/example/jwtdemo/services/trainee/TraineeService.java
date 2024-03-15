package com.example.jwtdemo.services.trainee;

import com.example.jwtdemo.entities.Trainee;
import com.example.jwtdemo.models.dto.TraineeDto;
import com.example.jwtdemo.models.profiles.TrainerProfile;
import com.example.jwtdemo.models.requests.patchRequest.PatchTrainee;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTrainee;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTraineeTrainers;
import com.example.jwtdemo.services.DaoService;

import java.util.List;

public interface TraineeService  extends DaoService<Trainee, TraineeDto,Long> {

    void setActiveState(PatchTrainee request);

    TraineeDto update(UpdateTrainee request);

    List<TrainerProfile> updateTraineeTrainers(UpdateTraineeTrainers request);
}
