package com.youngineer.backend.dto.responses;

import java.util.Date;

public record UserQuizDetails(String title, String createdAt, String updatedAt) {

    @Override
    public String toString() {
        return "UserQuizDetails{" +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
