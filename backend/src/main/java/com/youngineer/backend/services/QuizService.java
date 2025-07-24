package com.youngineer.backend.services;

import com.youngineer.backend.dto.requests.QuizInfo;
import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.requests.QuizResultRequest;
import com.youngineer.backend.dto.responses.QuizResultResponse;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.models.Quiz;
import com.youngineer.backend.models.User;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface QuizService {
    ResponseEntity<ResponseDto> generateQuiz(String emailId, QuizRequest generateQuizRequest);
    ResponseEntity<ResponseDto> calculateScore(String emailId, QuizResultRequest request);
    ResponseEntity<ResponseDto> getQuizInfo(String emailId, QuizInfo request);
    ResponseEntity<ResponseDto> getUserDashboardData(String emailId);
    ResponseEntity<ResponseDto> loadQuiz(String emailId, Long quizId);
    ResponseEntity<ResponseDto> isQuizResultGenerated(String emailId, Long quizId);
}
