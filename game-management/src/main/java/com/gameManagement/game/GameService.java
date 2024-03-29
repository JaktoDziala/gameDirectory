package com.gameManagement.game;

import com.gameManagement.annotation.ExampleOnly;
import com.gameManagement.exceptions.InvalidDTOValueException;
import com.gameManagement.exceptions.InvalidDateFormatException;
import com.gameManagement.exceptions.NullObjectException;
import com.gameManagement.exceptions.ObjectNotFoundException;
import com.gameManagement.exceptions.OutOfBoundsRatingException;
import com.gameManagement.game.enums.Genre;
import com.gameManagement.game.enums.Platform;
import com.gameManagement.studio.Studio;
import com.gameManagement.studio.StudioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final StudioService studioService;

    public GameService(GameRepository gameRepository, StudioService studioService) {
        this.gameRepository = gameRepository;
        this.studioService = studioService;
    }

    public Game addGame(GameDTO gameDTO) {
        Studio studio = null;
        if (gameDTO.studioId() != null) {
            studio = studioService.getStudio(gameDTO.studioId());
        }

        return gameRepository.save(
                new Game(
                        gameDTO.title(),
                        gameDTO.description(),
                        parseGameReleaseDate(gameDTO.releaseDate()),
                        parsePlatform(gameDTO.platform()),
                        studio,
                        parseGenre(gameDTO.genre())
                )
        );
    }

    public Game getGame(int gameId) {
        return gameRepository.findById(gameId).
                orElseThrow(() -> new ObjectNotFoundException("Game with Id " + gameId + " was not found"));
    }

    List<Game> getGames() {
        return gameRepository.findAll();
    }

    Game updateGame(int gameId, GameDTO gameDTO) {
        Game currentGame = getGame(gameId);

        if (gameDTO.platform() != null) {
            currentGame.setPlatform(
                    parsePlatform(gameDTO.platform()));
        }
        if (gameDTO.genre() != null) {
            currentGame.setGenre(
                    parseGenre(gameDTO.genre()));
        }
        if (gameDTO.releaseDate() != null) {
            currentGame.setReleaseDate(
                    parseGameReleaseDate(gameDTO.releaseDate()));
        }
        if (gameDTO.title() != null) {
            currentGame.setTitle(gameDTO.title());
        }
        if (gameDTO.description() != null) {
            currentGame.setDescription(gameDTO.description());
        }
        gameRepository.save(currentGame);
        return currentGame;
    }

    // TODO Check correct way of PUTTING vs PATCHING.
    @ExampleOnly
    Game patchGame(int gameId, GameDTO gameDTO) {
        Game currentGame = getGame(gameId);

        if (gameDTO.platform() != null) {
            currentGame.setPlatform(
                    parsePlatform(gameDTO.platform()));
        }
        if (gameDTO.genre() != null) {
            currentGame.setGenre(
                    parseGenre(gameDTO.genre()));
        }
        if (gameDTO.releaseDate() != null) {
            currentGame.setReleaseDate(
                    parseGameReleaseDate(gameDTO.releaseDate()));
        }
        if (gameDTO.title() != null) {
            currentGame.setTitle(gameDTO.title());
        }
        if (gameDTO.description() != null) {
            currentGame.setDescription(gameDTO.description());
        }
        gameRepository.save(currentGame);
        return currentGame;
    }

    float patchRating(int gameId, int newPartialRating) {
        return updateGameRating(getGame(gameId), newPartialRating);
    }

    public float patchRating(Game game, int newPartialRating) {
        return updateGameRating(game, newPartialRating);
    }

    float updateGameRating(Game game, int newPartialRating) throws OutOfBoundsRatingException {
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

    @Transactional
    String assignToStudio(int gameId, int studioId) {
        Game game = getGame(gameId);
        Studio studio = studioService.getStudio(studioId);

        Set<Game> games = studio.getGames();

        if (!games.contains(game)) {
            Set<Game> gamesToPersist = new HashSet<>(games.size() + 1);
            gamesToPersist.add(game);
            studio.setGames(gamesToPersist);
        }

        game.setStudio(studio);
        return "Game: \"" + game.getTitle() + "\" studio set to " +
                "Studio: \"" + studio.getName() + "\"";
    }

    Platform parsePlatform(String platform) {
        try {
            return Platform.valueOf(platform);
        } catch (Exception e) {
            throw new InvalidDTOValueException("Platform of gameDTO is of invalid type! Use one of the following types: "
                    + Arrays.stream(Platform.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", ")));
        }
    }

    Genre parseGenre(String genre) {
        try {
            return Genre.valueOf(genre);
        } catch (Exception e) {
            throw new InvalidDTOValueException("Genre of gameDTO is of invalid type! Use one of the following types: "
                    + Arrays.stream(Genre.values()).map(
                            Genre::name)
                    .collect(Collectors.joining(", ")));
        }
    }

    LocalDate parseGameReleaseDate(String releaseDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        try {
            return LocalDate.parse(releaseDate, dateTimeFormatter);
        } catch (DateTimeException e) {
            throw new InvalidDateFormatException("Invalid date format! " +
                    "Provide date in expected format: yyyy-MM-dd");
        }
    }

    //region [Functionality testing methods. Ignore for development.]

    /**
     * This method does not automatically persist data as it is not transactional.
     *
     * <p>Despite this, integration test result for this method suggest transactional behavior.
     * This discrepancy arises because of <b>@DataJpaTest</b> annotation,
     * which treats entire test class as transactional by default.
     * It leads to results that mimic transactional behavior for non-transactional methods.
     *
     * <p>To enable automatic data flushing in real behaviour, use the methods with <b>@Transactional</b> annotation.
     * Remember that all methods within a class annotated with <b>@DataJpaTest</b> are treated as if they are transactional.
     */
    @ExampleOnly
    String assignToStudioNoTransaction(int gameId, int studioId) {
        Game game = getGame(gameId);
        Studio studio = studioService.getStudio(studioId);

        Set<Game> games = studio.getGames();

        if (!games.contains(game)) {
            Set<Game> gamesToNotPersist = new HashSet<>(games.size() + 1);
            gamesToNotPersist.add(game);
            studio.setGames(gamesToNotPersist);
        }

        game.setStudio(studio);
        return "Game: \"" + game.getTitle() + "\" studio set to " +
                "Studio: \"" + studio.getName() + "\"";
    }

    /**
     * Issue: I suspected that unmodifiable list is not needed for List getters, since hibernate is blocking modifications that way by itself.
     * This suspicion arouse during work on {@link GameService#assignToStudio(int, int)} method,
     * which disallowed me to modify list directly on integration test.
     *
     * <p>Answer: Suspicion is invalid in real life. It is possible to add elements to getter that is not set to unmodifiable.
     * Issue tested via postman.
     */
    @ExampleOnly
    @Transactional
    String modifyGameListFromGetter() {
        Game game = getGame(1);
        Studio studio = studioService.getStudio(1);
        studio.getGames().add(game);
        game.setStudio(studio);
        Set<Game> games2 = studio.getGames();
        return games2.toString();
    }
    //endregion
}
