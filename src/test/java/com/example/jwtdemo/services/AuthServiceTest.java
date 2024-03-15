package com.example.jwtdemo.services;

import com.example.jwtdemo.auth.AuthenticateService;
import com.example.jwtdemo.entities.User;
import com.example.jwtdemo.models.requests.authRequest.Authentication;
import com.example.jwtdemo.models.requests.authRequest.ChangePassword;
import com.example.jwtdemo.models.requests.registrationRequest.UserRegistration;
import com.example.jwtdemo.models.responses.AuthenticationResponse;
import com.example.jwtdemo.repositories.UserRepository;
import com.example.jwtdemo.config.JwtTokenService;
import com.example.jwtdemo.auth.CredentialConfigurer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private CredentialConfigurer credentialConfigurer;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthenticateService authenticateService;

    @Test
    public void testRegister() {

        UserRegistration request = UserRegistration
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("em.email@.com")
                .password("strongPass")
                .IsActive(true)
                .build();
        User user = mock(User.class);

        when(credentialConfigurer.generateUniqueUsername(any(), any()))
                .thenReturn("username");

        when(passwordEncoder.encode(any()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any()))
                .thenReturn(user);

        when(jwtTokenService.generateToken(any()))
                .thenReturn("generatedToken");


        AuthenticationResponse response = authenticateService.register(request);


        assertEquals("generatedToken", response.getToken());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void testAuthenticate() {

        Authentication request = Authentication.builder()
                .email("domain@gmail.com")
                .password("pass")
                .build();

        User user = mock(User.class);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(jwtTokenService.generateToken(any()))
                .thenReturn("generatedToken");


        AuthenticationResponse response = authenticateService.authenticate(request);


        assertEquals("generatedToken", response.getToken());
    }

    @Test
    public void testChangePasswordAndAuthenticate() {

        ChangePassword request = ChangePassword.builder()
                .email("email")
                .oldPassword("old")
                .newPassword("new")
                .build();
        User user = mock(User.class);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode(any()))
                .thenReturn("encodedPassword");

        when(passwordEncoder.matches(any(),any()))
                .thenReturn(true);

        when(jwtTokenService.generateToken(any()))
                .thenReturn("generatedToken");


        AuthenticationResponse response = authenticateService.changePasswordAndAuthenticate(request);


        assertEquals("generatedToken", response.getToken());

        verify(user, times(1)).setPassword(any());

        verify(userRepository, times(1)).save(any());
    }
}
