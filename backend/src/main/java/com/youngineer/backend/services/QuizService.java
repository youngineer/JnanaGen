package com.youngineer.backend.services;

import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface QuizService {
    public ResponseDto generateQuiz(QuizRequest generateQuizRequest);
    public ResponseDto generateScore(List<String> answers);
}
