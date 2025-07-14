package com.youngineer.backend.dto.requests;

import org.springframework.lang.NonNull;

import java.util.List;

public record UserQuizResultRequest(
        @NonNull
        List<String> answers
) {
}
