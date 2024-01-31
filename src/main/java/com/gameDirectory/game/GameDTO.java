package com.gameDirectory.game;

import jakarta.validation.constraints.NotBlank;

public record GameDTO(
        @NotBlank(message = "Title cannot be blank!")
        String title,
        @NotBlank(message = "Description cannot be blank!")
        String description,
        @NotBlank(message = "Release date cannot be blank!")
        String releaseDate,
        String platform,
        String genre,
        Integer studioId
) {
}
