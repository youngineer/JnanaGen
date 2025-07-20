package com.youngineer.backend.dto.responses;

public record AnswerEvaluation(
        String question,
        String userAnswer,
        String correctAnswer,
        Boolean isCorrect,
        String explanation
) {

}
