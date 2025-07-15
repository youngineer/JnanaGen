package com.youngineer.backend.dto.responses;

import jakarta.validation.constraints.NotNull;

public record UserQuizResultResponse(
        @NotNull
        Integer score
) {
}
