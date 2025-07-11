package com.youngineer.backend.controllers;

import com.youngineer.backend.dto.requests.UserSignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseDto userSignUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        return userService.saveUser(userSignUpRequest);
    }
}
