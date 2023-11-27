package com.game.gameDirectory.game;

import com.game.gameDirectory.exceptions.NullObjectException;
import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import com.game.gameDirectory.exceptions.OutOfBoundsRatingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void addGame(Game game) {
        gameRepository.save(game);
    }

    public Game getGame(int gameId) {
        return gameRepository.findById(gameId).
                orElseThrow(() -> new ObjectNotFoundException("Game with Id " + gameId + " was not found"));
    }

    List<Game> getGames() {
        return gameRepository.findAll();
    }

    void updateGame(int gameId, Game game) {
        Game currentGame = getGame(gameId);
        currentGame.setPlatform(game.getPlatform());
        currentGame.setDescription(game.getDescription());
        currentGame.setTitle(game.getTitle());
        currentGame.setReleaseDate(game.getReleaseDate());
        // Game rating and count gets updated by patch method
        gameRepository.save(currentGame);
    }

    float patchRating(int gameId, int newPartialRating) {
        return updateGameRating(getGame(gameId), newPartialRating);
    }

    public float patchRating(Game game, int newPartialRating) {
        return updateGameRating(game, newPartialRating);
    }

    float updateGameRating(Game game, int newPartialRating) throws OutOfBoundsRatingException {
        if (newPartialRating > 10 || newPartialRating < 1)
            throw new OutOfBoundsRatingException(newPartialRating);
        game.setReviewCount(game.getReviewCount() + 1);

        float oldRating = game.getRating();
        float reviewCount = game.getReviewCount();
        float newRating = oldRating + (newPartialRating - oldRating) / reviewCount;

        game.setRating(newRating);
        gameRepository.save(game);

        return newRating;
    }

    public void deleteGame(int gameId) {
        gameRepository.findById(gameId).orElseThrow(() -> new ObjectNotFoundException("Game with id " + gameId + " could not be found for deletion!"));
        gameRepository.deleteById(gameId);
    }
    public void deleteGame(Game game) {
        if (game != null) {
            deleteGame(game.getId());
        }else {
            throw new NullObjectException(Game.class);
        }
    }

    void deleteAll() {
        gameRepository.deleteAll();
    }
}
