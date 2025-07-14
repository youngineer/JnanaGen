package com.youngineer.backend.controllers;


import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final QuizService quizService;

    public UserController(QuizService quizService) {
        this.quizService = quizService;
    }


    @PostMapping("/generateQuiz")
    public ResponseDto generateQuiz(@Valid @RequestBody QuizRequest quizRequest) {
        return quizService.generateQuiz(quizRequest);
    }
}
