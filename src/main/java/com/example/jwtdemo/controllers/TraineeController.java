package com.example.jwtdemo.controllers;

import com.example.jwtdemo.models.dto.TraineeDto;
import com.example.jwtdemo.models.profiles.TrainerProfile;
import com.example.jwtdemo.models.requests.patchRequest.PatchTraineeRequest;
import com.example.jwtdemo.models.requests.registrationRequest.TraineeRegistrationRequest;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTraineeRequest;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTraineeTrainersRequest;
import com.example.jwtdemo.models.responses.RegistrationResponse;
import com.example.jwtdemo.services.trainee.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> create(@RequestBody TraineeRegistrationRequest request){
        return ResponseEntity.ok(traineeService.create(request));
    }
    @GetMapping("/{username}")
    public ResponseEntity<TraineeDto> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(traineeService.findByUsername(username));
    }

    @PostMapping("/trainers")
    public ResponseEntity<List<TrainerProfile>> updateTraineeTrainers(@RequestBody UpdateTraineeTrainersRequest request){
        return ResponseEntity.ok(traineeService.updateTraineeTrainers(request));
    }
    @PutMapping
    public ResponseEntity<TraineeDto> updateTrainee(@RequestBody UpdateTraineeRequest request) {
        return ResponseEntity.ok(traineeService.update(request));
    }
    @PatchMapping("/trainee/state")
    public ResponseEntity<?> updateTraineeState(@RequestBody PatchTraineeRequest request){
        traineeService.setActiveState(request);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{traineeId}")
    public ResponseEntity<String> delete(@PathVariable Long traineeId){
        traineeService.delete(traineeId);
        return ResponseEntity.ok().build();
    }



}
