package com.gameDirectory.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record ReviewDTO(
        @NotNull(message = "gameId is required")
        Integer gameId,
        @NotBlank(message = "comment is required")
        String comment,
        @NotNull(message = "rating is required")
        @Min(value = 1, message = "rating must be at least 1")
        @Max(value = 10, message = "rating must be at most 10")
        Integer rating) {
}
