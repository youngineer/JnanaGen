package com.youngineer.backend.controllers;


import com.sun.security.auth.UserPrincipal;
import com.youngineer.backend.dto.requests.QuizInfo;
import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.requests.QuizResultRequest;
import com.youngineer.backend.dto.requests.UserIdRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.QuizService;
import com.youngineer.backend.utils.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final QuizService quizService;

    public UserController(QuizService quizService) {
        this.quizService = quizService;
    }


    @PostMapping("/generateQuiz")
    public ResponseDto generateQuiz(@Valid @RequestBody QuizRequest quizRequest, @CookieValue(name = "accessToken", defaultValue = "") String token) {
        String emailId = JwtHelper.extractEmailId(token);
        return quizService.generateQuiz(emailId, quizRequest);
    }


    @PostMapping("/calculateScore")
    public ResponseDto getQuizResult(@Valid @RequestBody QuizResultRequest request, @CookieValue(name = "accessToken", defaultValue = "") String token) {
        String emailId = JwtHelper.extractEmailId(token);
        return quizService.calculateScore(emailId, request);
    }

    @PostMapping("/getQuizInfo")
    public ResponseDto getQuizResult(@Valid @RequestBody QuizInfo request, @CookieValue(name = "accessToken", defaultValue = "") String token) {
        String emailId = JwtHelper.extractEmailId(token);
        return quizService.getQuizInfo(emailId, request);
    }

    @PostMapping("/getUserDashboardData")
    public ResponseDto getUserDashboardData(@Valid @CookieValue(name = "accessToken", defaultValue = "") String token) {
        String emailId = JwtHelper.extractEmailId(token);
        return quizService.getUserDashboardData(emailId);
    }

}
