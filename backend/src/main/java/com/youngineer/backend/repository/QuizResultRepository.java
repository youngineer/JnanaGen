package com.youngineer.backend.repository;

import com.youngineer.backend.models.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
}
