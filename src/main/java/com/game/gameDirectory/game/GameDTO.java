package com.game.gameDirectory.game;

public record GameDTO(
        String title,
        String description,
        String releaseDate,
        String platform,
        String genre,
        Integer studioId
) {
}
