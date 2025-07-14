package com.youngineer.backend.dto.responses;

import org.springframework.lang.NonNull;

public record OptionDto(

        @NonNull
        Long id,

        @NonNull
        String option
) {
}
