package com.youngineer.backend.controllers;


import com.youngineer.backend.dto.requests.QuizInfo;
import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.requests.QuizResultRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/user")
public class UserController {

    private final QuizService quizService;

    public UserController(QuizService quizService) {
        this.quizService = quizService;
    }

    private String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Retrieves email from the authenticated user
    }

    @PostMapping("/generateQuiz")
    public ResponseEntity<ResponseDto> generateQuiz(@Valid @RequestBody QuizRequest quizRequest) {
        String emailId = getLoggedInUserEmail();
        return quizService.generateQuiz(emailId, quizRequest);
    }

    @GetMapping("/loadQuiz/{quizId}")
    public ResponseEntity<ResponseDto> loadQuiz(@PathVariable Long quizId) {
        String emailId = getLoggedInUserEmail();
        return quizService.loadQuiz(emailId, quizId);
    }

    @PostMapping("/calculateScore")
    public ResponseEntity<ResponseDto> getQuizResult(@Valid @RequestBody QuizResultRequest request) {
        String emailId = getLoggedInUserEmail();
        return quizService.calculateScore(emailId, request);
    }

    @PostMapping("/getQuizInfo")
    public ResponseEntity<ResponseDto> getQuizResult(@Valid @RequestBody QuizInfo request) {
        String emailId = getLoggedInUserEmail();
        return quizService.getQuizInfo(emailId, request);
    }

    @GetMapping("/getUserQuizzes")
    public ResponseEntity<ResponseDto> getUserDashboardData() {
        String emailId = getLoggedInUserEmail();
        return quizService.getUserDashboardData(emailId);
    }

    @GetMapping("/getRedirectionPath/{quizId}")
    public ResponseEntity<ResponseDto> getRedirectionPath(@Valid @PathVariable Long quizId) {
        String emailId = getLoggedInUserEmail();
        return quizService.isQuizResultGenerated(emailId, quizId);
    }
}
