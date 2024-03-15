package com.example.jwtdemo.controllers;

import com.example.jwtdemo.models.dto.TraineeTrainingDto;
import com.example.jwtdemo.models.dto.TrainerTrainingDto;
import com.example.jwtdemo.models.requests.registrationRequest.TrainingRegistrationRequest;
import com.example.jwtdemo.models.requests.trainingFilterRequest.TraineeTrainingRequest;
import com.example.jwtdemo.models.requests.trainingFilterRequest.TrainerTrainingRequest;
import com.example.jwtdemo.services.training.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    public ResponseEntity<?> createTraining(@RequestBody TrainingRegistrationRequest request){

        trainingService.createTraining(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainee-trainings")
    public ResponseEntity<List<TraineeTrainingDto>> getTraineeTrainingsList(@RequestBody TraineeTrainingRequest request){

        var trainings = trainingService.getTraineeTrainings(request);

        return ResponseEntity.ok(trainings);
    }
    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<TrainerTrainingDto>> getTrainerTrainingsList(@RequestBody TrainerTrainingRequest request){

        var trainings = trainingService.getTrainerTrainings(request);

        return ResponseEntity.ok(trainings);
    }
}
