package com.example.jwtdemo.models.requests.updateRequest;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTraineeTrainers {
    private String traineeUsername;
    private List<String> trainers;
}
