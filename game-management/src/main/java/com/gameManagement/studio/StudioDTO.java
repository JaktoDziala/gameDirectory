package com.gameManagement.studio;

import com.gameManagement.game.Game;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record StudioDTO(
        @NotBlank(message = "Name cannot be blank!")
        String name,
        @NotBlank(message = "Description cannot be blank!")
        String description,
        Set<Game> games) {
}
