package com.youngineer.backend.dto.responses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResponseDto(
        @NotNull
        @NotBlank
        String message,

        @NotBlank
        Object content
) {
}
