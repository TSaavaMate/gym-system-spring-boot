package com.example.jwtdemo.models.requests.registrationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistration implements Registration {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Boolean IsActive;
    private String role;
}
