package com.youngineer.backend.dto.responses;

import com.youngineer.backend.models.Quiz;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashMap;
import java.util.List;

public record DashboardDto(
        Long id,
        @NotNull
        @NotBlank
        String name,

        @NotNull
        @NotBlank
        String emailId,

        LinkedHashMap<Long, UserQuizDetails> userQuizzes
) {
}
