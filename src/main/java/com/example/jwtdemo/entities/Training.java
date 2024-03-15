package com.example.jwtdemo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Date date;


    @NonNull
    private Integer duration;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;


    @ManyToOne
    @JoinColumn(name = "training_Type")
    private TrainingType trainingType;

}

