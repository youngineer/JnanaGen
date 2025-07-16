package com.youngineer.backend.services;

import com.youngineer.backend.dto.requests.QuizInfo;
import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.requests.QuizResultRequest;
import com.youngineer.backend.dto.responses.QuizResultResponse;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.models.Quiz;
import com.youngineer.backend.models.User;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface QuizService {
    public ResponseDto generateQuiz(QuizRequest generateQuizRequest);
    public ResponseDto calculateScore(QuizResultRequest request);
    public ResponseDto getQuizInfo(QuizInfo request);
    public ResponseDto getUserQuizDetails(Long userId);
}
