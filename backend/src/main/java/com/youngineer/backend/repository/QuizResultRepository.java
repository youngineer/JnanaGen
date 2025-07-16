package com.youngineer.backend.repository;

import com.youngineer.backend.models.Quiz;
import com.youngineer.backend.models.QuizResult;
import com.youngineer.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findAllByUserAndQuiz(User user, Quiz quiz);
}
