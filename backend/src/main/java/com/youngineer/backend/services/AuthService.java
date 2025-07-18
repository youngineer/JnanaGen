package com.youngineer.backend.services;

import com.youngineer.backend.dto.requests.LoginRequest;
import com.youngineer.backend.dto.requests.SignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {
    ResponseDto signup(SignUpRequest signUpRequest);
    String login(LoginRequest loginRequest);
}
