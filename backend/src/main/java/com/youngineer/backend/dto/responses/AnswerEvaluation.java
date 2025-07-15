package com.youngineer.backend.dto.responses;

public class AnswerEvaluation {
    private final Boolean isCorrect;
    private final String correctAnswer;

    public AnswerEvaluation(Boolean isCorrect, String correctAnswer) {
        this.isCorrect = isCorrect;
        this.correctAnswer = correctAnswer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }


    @Override
    public String toString() {
        return "AnswerEvaluation{" +
                "isCorrect=" + isCorrect +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
