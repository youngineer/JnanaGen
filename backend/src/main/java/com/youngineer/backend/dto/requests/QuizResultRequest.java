package com.youngineer.backend.dto.requests;

import org.springframework.lang.NonNull;

import java.util.LinkedHashMap;
import java.util.List;

public record QuizResultRequest(
        @NonNull
        Long quizId,

        @NonNull
        LinkedHashMap<Long, Long> questionOptionMap,

        @NonNull
        List<String> userAnswerList
) {
}
