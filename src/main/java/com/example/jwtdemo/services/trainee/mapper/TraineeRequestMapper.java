package com.example.jwtdemo.services.trainee.mapper;

import com.example.jwtdemo.entities.Trainee;
import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.models.requests.registrationRequest.TraineeRegistration;
import com.example.jwtdemo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TraineeRequestMapper implements Function<TraineeRegistration, Trainee> {
    private final UserRepository userRepository;
    @Override
    public Trainee apply(TraineeRegistration request) {
        var user = userRepository.findByFirstNameAndLastName(request.getFirstname(), request.getLastname())
                .orElseThrow(ResourceNotFoundException::new);
        return new Trainee(request.getDateOfBirth(),request.getAddress(),user);
    }
}
