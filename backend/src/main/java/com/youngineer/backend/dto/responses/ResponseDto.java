package com.youngineer.backend.dto.responses;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

public record ResponseDto(
        @NonNull
        @NotBlank
        String message,

        @NotBlank
        Object content
) {
}
