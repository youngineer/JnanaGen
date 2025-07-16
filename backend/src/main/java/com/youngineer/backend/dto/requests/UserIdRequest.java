package com.youngineer.backend.dto.requests;

import org.springframework.lang.NonNull;

public record UserIdRequest(
        @NonNull
        Long userId
) {
}
