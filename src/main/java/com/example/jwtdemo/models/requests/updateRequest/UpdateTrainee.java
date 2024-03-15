package com.example.jwtdemo.models.requests.updateRequest;

import lombok.*;


import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTrainee {
    @NonNull 
    private String username;
    @NonNull
    private String firstname;
    @NonNull
    private String lastname;
    private Date date_of_birth;
    private String address;
    @NonNull
    private Boolean isActive;
}
