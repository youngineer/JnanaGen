package com.youngineer.backend.dto.responses;

public class AnswerEvaluation {
    private final Boolean isCorrect;
    private final String correctAnswer;
    private final String question;
    private final String userAnswer;

    public AnswerEvaluation(String question, String userAnswer, String correctAnswer, Boolean isCorrect) {
        this.isCorrect = isCorrect;
        this.correctAnswer = correctAnswer;
        this.question = question;
        this.userAnswer = userAnswer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    @Override
    public String toString() {
        return "AnswerEvaluation{" +
                "isCorrect=" + isCorrect +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", question='" + question + '\'' +
                ", userAnswer='" + userAnswer + '\'' +
                '}';
    }
}
