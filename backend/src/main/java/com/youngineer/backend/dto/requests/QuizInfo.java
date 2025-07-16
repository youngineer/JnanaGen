package com.youngineer.backend.dto.requests;

import org.springframework.lang.NonNull;

public record QuizInfo(
        @NonNull
        Long userId,

        @NonNull
        Long quizId
) {
}
