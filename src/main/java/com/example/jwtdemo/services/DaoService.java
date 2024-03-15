package com.example.jwtdemo.services;

import com.example.jwtdemo.models.requests.registrationRequest.Registration;
import com.example.jwtdemo.models.responses.RegistrationResponse;

import java.util.Collection;
import java.util.Optional;

public interface DaoService <T, D, K>{
    Optional<T> findById(K id);
    D findByUsername(String username);

    RegistrationResponse create(Registration request);

    Collection<T> findAll();

    void delete(K id);
}
