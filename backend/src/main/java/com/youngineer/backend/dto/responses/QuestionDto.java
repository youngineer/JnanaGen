package com.youngineer.backend.dto.responses;

import org.springframework.lang.NonNull;

import java.util.List;

public record QuestionDto(
        @NonNull
        Long id,

        @NonNull
        String question,

        @NonNull
        List<OptionDto> options
) {
}
