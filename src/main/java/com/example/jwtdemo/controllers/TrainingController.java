package com.example.jwtdemo.controllers;

import com.example.jwtdemo.models.dto.TraineeTrainingDto;
import com.example.jwtdemo.models.dto.TrainerTrainingDto;
import com.example.jwtdemo.models.requests.registrationRequest.TrainingRegistration;
import com.example.jwtdemo.models.requests.trainingFilterRequest.TraineeTraining;
import com.example.jwtdemo.models.requests.trainingFilterRequest.TrainerTraining;
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
    public ResponseEntity<?> createTraining(@RequestBody TrainingRegistration request){

        trainingService.createTraining(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainee-trainings")
    public ResponseEntity<List<TraineeTrainingDto>> getTraineeTrainingsList(@RequestBody TraineeTraining request){

        var trainings = trainingService.getTraineeTrainings(request);

        return ResponseEntity.ok(trainings);
    }
    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<TrainerTrainingDto>> getTrainerTrainingsList(@RequestBody TrainerTraining request){

        var trainings = trainingService.getTrainerTrainings(request);

        return ResponseEntity.ok(trainings);
    }
}
