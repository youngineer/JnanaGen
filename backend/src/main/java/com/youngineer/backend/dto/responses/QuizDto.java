package com.youngineer.backend.dto.responses;

import org.springframework.lang.NonNull;

import java.util.List;

public record QuizDto(
        @NonNull
        Long id,

        @NonNull
        String title,

        @NonNull
        List<QuestionDto> questions
) {
}
