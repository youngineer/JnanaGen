package com.youngineer.backend.dto.requests;

import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.List;

public record UserQuizResultRequest(
        @NonNull
        Long quizId,

        @NonNull
        HashMap<Long, Long> questionOptionMap,

        @NonNull
        List<String> answerOptionList
) {
}
