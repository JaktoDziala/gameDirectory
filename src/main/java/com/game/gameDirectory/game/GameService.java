package com.game.gameDirectory.game;

import com.game.gameDirectory.exceptions.InvalidDTOValueException;
import com.game.gameDirectory.exceptions.NullObjectException;
import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import com.game.gameDirectory.exceptions.OutOfBoundsRatingException;
import com.game.gameDirectory.game.enums.Genre;
import com.game.gameDirectory.game.enums.Platform;
import com.game.gameDirectory.studio.Studio;
import com.game.gameDirectory.studio.StudioService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final StudioService studioService;

    public GameService(GameRepository gameRepository, StudioService studioService) {

        this.gameRepository = gameRepository;
        this.studioService = studioService;
    }

    public Game addGame(GameDTO gameDTO) {
        return gameRepository.save(
                validateDTO(gameDTO)
        );
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
        getGame(gameId);
        gameRepository.deleteById(gameId);
    }

    public void deleteGame(Game game) {
        if (game != null) {
            deleteGame(game.getId());
        } else {
            throw new NullObjectException(Game.class);
        }
    }

    void deleteAll() {
        gameRepository.deleteAll();
    }

    // TODO: Rozważ interfejsy
    // TODO: Fix date to use not deprecated type
    Game validateDTO(GameDTO gameDTO) {

        Game game = new Game();

        try {
            game.setPlatform(
                    Platform.valueOf(gameDTO.platform()));
        } catch (Exception e) {
            throw new InvalidDTOValueException("Platform of gameDTO is of invalid type!");
        }

        try {
            game.setGenre(
                    Genre.valueOf(gameDTO.genre())
            );
        } catch (Exception e) {
            throw new InvalidDTOValueException("Genre of gameDTO is of invalid type!");
        }

        if (StringUtils.isBlank(gameDTO.title())) {
            throw new InvalidDTOValueException("Title of gameDTO is Blank!");
        }
        if (StringUtils.isBlank(gameDTO.description())) {
            throw new InvalidDTOValueException("Description of gameDTO is Blank!");
        }
        if (StringUtils.isBlank(gameDTO.releaseDate())) {
            throw new InvalidDTOValueException("Release Date of gameDTO is Blank!");
        }

        game.setTitle(gameDTO.title());
        game.setDescription(gameDTO.description());
        game.setReleaseDate(new Date(gameDTO.releaseDate()));
        game.setStudio(studioService.getStudio(gameDTO.studioId()));

        return game;
    }
}
