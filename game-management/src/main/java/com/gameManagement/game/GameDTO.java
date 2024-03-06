package com.gameManagement.game;

import jakarta.validation.constraints.NotBlank;

public record GameDTO(
        @NotBlank(message = "Title cannot be blank!")
        String title,
        @NotBlank(message = "Description cannot be blank!")
        String description,
        @NotBlank(message = "Release date cannot be blank!")
        String releaseDate,
        @NotBlank(message = "Platform is required!")
        String platform,
        @NotBlank(message = "Genre is required!")
        String genre,
        Integer studioId
) {
}
