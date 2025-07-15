package com.youngineer.backend.dto.responses;
import org.springframework.lang.NonNull;

import java.util.List;

public record QuizResultResponse(
        @NonNull
        Integer score,

        @NonNull
        List<String> correctAnswerList
) {
}
