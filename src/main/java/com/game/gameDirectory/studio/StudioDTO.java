package com.game.gameDirectory.studio;

import com.game.gameDirectory.game.Game;

import java.util.List;

public record StudioDTO(String description, List<Game> games) {
}
