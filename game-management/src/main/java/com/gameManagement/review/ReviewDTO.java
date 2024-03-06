package com.gameManagement.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record ReviewDTO(
        @NotNull(message = "GameId is required!")
        Integer gameId,
        @NotBlank(message = "Comment is required!")
        String comment,
        @NotNull(message = "Rating is required!")
        @Min(value = 1, message = "Rating must be at least 1!")
        @Max(value = 10, message = "Rating must be at most 10!")
        Integer rating) {
}
