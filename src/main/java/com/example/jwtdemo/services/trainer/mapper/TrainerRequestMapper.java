package com.example.jwtdemo.services.trainer.mapper;

import com.example.jwtdemo.entities.Trainer;
import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.models.requests.registrationRequest.TrainerRegistration;
import com.example.jwtdemo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TrainerRequestMapper implements Function<TrainerRegistration, Trainer> {
    private final UserRepository userRepository;
    @Override
    public Trainer apply(TrainerRegistration request) {
        var user = userRepository.findByFirstNameAndLastName(request.getFirstname(), request.getLastname())
                .orElseThrow(ResourceNotFoundException::new);
        return new Trainer(user,request.getSpecialization());
    }
}
