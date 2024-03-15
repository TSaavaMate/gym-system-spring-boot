package com.example.jwtdemo.models.requests.updateRequest;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTrainer {
    @NonNull
    private String username;
    @NonNull
    private String firstname;
    @NonNull
    private String lastname;
    private String specialization;
    @NonNull
    private Boolean isActive;
}
