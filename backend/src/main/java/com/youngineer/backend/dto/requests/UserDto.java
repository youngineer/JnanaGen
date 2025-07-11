package com.youngineer.backend.dto.requests;

import com.youngineer.backend.models.Quiz;
import com.youngineer.backend.models.QuizResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDto(
        @NotNull
        @NotBlank
        String name,

        @NotNull
        @NotBlank
        String emailId,

        List<Quiz> userQuizzes,
        List<QuizResult> userQuizResults
) {
}
