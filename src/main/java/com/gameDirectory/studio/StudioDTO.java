package com.gameDirectory.studio;

import com.gameDirectory.game.Game;

import java.util.List;

public record StudioDTO(String name, String description, List<Game> games) {
}
