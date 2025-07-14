package com.youngineer.backend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record GenerateQuizRequest(
        @NotBlank
        @NotNull
        String userNotes,

        @NotNull
        Integer totalQuestions,

        @NotNull
        Integer numberOfOptions,

        @DefaultValue("")
        String additionalContext
) {
}

