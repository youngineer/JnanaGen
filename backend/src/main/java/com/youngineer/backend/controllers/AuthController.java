package com.youngineer.backend.controllers;

import com.youngineer.backend.dto.requests.LoginRequest;
import com.youngineer.backend.dto.requests.SignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.AuthService;
import com.youngineer.backend.utils.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> userLogin(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.emailId(), request.password()));
        System.out.println("auth successful");
        String token = JwtHelper.generateToken(request.emailId());
        System.out.println(token);
        return ResponseEntity.ok(new ResponseDto(request.emailId(), token));
    }
}
