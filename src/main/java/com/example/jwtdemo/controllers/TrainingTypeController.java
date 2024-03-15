package com.example.jwtdemo.controllers;

import com.example.jwtdemo.models.dto.TrainingTypeDto;
import com.example.jwtdemo.services.trainingtype.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trainingTypes")
@RequiredArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @GetMapping
    public ResponseEntity<List<TrainingTypeDto>> findAllTrainingTypes(){
        return ResponseEntity.ok(trainingTypeService.findAll());
    }
}
