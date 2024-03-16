package com.example.jwtdemo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Trainee extends Person{
    public Trainee(@NonNull Date dateOfBirth, @NonNull String address, User user) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }

    @NonNull
    private Date dateOfBirth;

    @NonNull
    private String address;


}
