package com.example.jwtdemo.models.requests.registrationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeRegistration implements Registration {
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String address;
}
