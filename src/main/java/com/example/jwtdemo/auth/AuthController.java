package com.example.jwtdemo.auth;

import com.example.jwtdemo.models.requests.authRequest.Authentication;
import com.example.jwtdemo.models.requests.authRequest.ChangePassword;
import com.example.jwtdemo.models.requests.registrationRequest.UserRegistration;
import com.example.jwtdemo.models.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserRegistration request
    ){
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody Authentication request
    ){
        return ResponseEntity.ok(service.authenticate(request));
//        AuthenticationResponse authenticate = service.authenticate(request);
//        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePassword request
    ){
        service.changePasswordAndAuthenticate(request);
        return ResponseEntity.ok().build();
    }
}
