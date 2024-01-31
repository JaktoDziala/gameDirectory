package com.gameDirectory.studio;

import com.gameDirectory.game.Game;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record StudioDTO(
        @NotBlank(message = "Name cannot be blank!")
        String name,
        @NotBlank(message = "Description cannot be blank!")
        String description, List<Game> games) {
}
