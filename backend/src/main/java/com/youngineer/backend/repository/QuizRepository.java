package com.youngineer.backend.repository;

import com.youngineer.backend.models.Quiz;
import com.youngineer.backend.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByUser(User user, Sort sort);
}
