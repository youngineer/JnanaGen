package com.youngineer.backend.controllers;


import com.youngineer.backend.dto.requests.QuizInfo;
import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.requests.QuizResultRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.QuizService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final QuizService quizService;

    public UserController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/generateQuiz")
    public ResponseDto generateQuiz(@Valid @RequestBody QuizRequest quizRequest, Authentication authentication) {
        String emailId = authentication.getName();
        return quizService.generateQuiz(emailId, quizRequest);
    }


    @PostMapping("/calculateScore")
    public ResponseDto getQuizResult(@Valid @RequestBody QuizResultRequest request, Authentication authentication) {
        String emailId = authentication.getName();
        return quizService.calculateScore(emailId, request);
    }

    @PostMapping("/getQuizInfo")
    public ResponseDto getQuizResult(@Valid @RequestBody QuizInfo request, Authentication authentication) {
        String emailId = authentication.getName();
        return quizService.getQuizInfo(emailId, request);
    }

    @PostMapping("/getUserDashboardData")
    public ResponseDto getUserDashboardData(@Valid Authentication authentication) {
        String emailId = authentication.getName();
        return quizService.getUserDashboardData(emailId);
    }

}
