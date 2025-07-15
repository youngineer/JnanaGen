package com.youngineer.backend.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        @NotBlank
        @Email
        String emailId,

        @NotNull
        @NotBlank
        String password
) {
}
