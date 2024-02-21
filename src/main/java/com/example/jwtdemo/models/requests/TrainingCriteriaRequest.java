package com.example.jwtdemo.models.requests;

import com.example.jwtdemo.entities.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingCriteriaRequest {
    private String username;
    private Date periodFrom;
    private Date periodTo;
    private String trainerFirstName;
    private TrainingType trainingType;
}
