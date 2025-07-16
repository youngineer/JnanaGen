package com.youngineer.backend.dto.responses;
import org.springframework.lang.NonNull;

import java.util.LinkedHashMap;
import java.util.List;

public record QuizResultResponse(
        @NonNull
        Integer score,

        @NonNull
        LinkedHashMap<String, String> questionOptionMap,

        @NonNull
        LinkedHashMap<Long, AnswerEvaluation> evaluationMap
) {
}
