package com.example.jwtdemo.models.requests.trainingFilterRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeTraining {
    private String username;
    private Date periodFrom;
    private Date periodTo;
    private String trainerFirstName;
    private String trainingTypeName;
}
