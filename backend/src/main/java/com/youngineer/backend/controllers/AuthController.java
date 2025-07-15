package com.youngineer.backend.controllers;

import com.youngineer.backend.dto.requests.LoginRequest;
import com.youngineer.backend.dto.requests.SignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseDto userSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.userSignup(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseDto userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.userLogin(loginRequest);
    }
}
