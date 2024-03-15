package com.example.jwtdemo.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Trainer extends Person{
    public Trainer(User user, String specialization) {
        this.user = user;
        this.specialization = specialization;
    }

    private String specialization;

}
