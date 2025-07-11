package com.youngineer.backend.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;

public record UserSignUpRequest(
        @NonNull
        @NotBlank
        String name,

        @NonNull
        @NotBlank
        @Email
        String emailId,

        @NonNull
        @NotBlank
        @Size(min = 4, message = "Please enter password of length 4 or more")
        String password
){}
