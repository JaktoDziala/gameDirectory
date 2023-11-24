package com.game.gameDirectory.game;

import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import com.game.gameDirectory.exceptions.OutOfBoundsRatingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    GameService sut;

    @Mock
    GameRepository gameRepository;

    private final int validGameId = 1;
    private final int invalidGameId = -1;
    Game sharedGame;

    @BeforeEach
    void setUp()
    {
        sharedGame = new Game();
    }

    @Test
    void addGame_WithValidObject_AddsGame() throws Exception {
        // given
        // when
        sut.addGame(sharedGame);

        // then
        verify(gameRepository).save(sharedGame);
    }

    @Test
    void getGame_WithValidId_ReturnsGame() {
        // given
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(sharedGame));

        // when
        Game result = sut.getGame(validGameId);

        // then
        verify(gameRepository).findById(1);
        assertEquals(sharedGame, result);
    }

    @Test
    void getGame_WithNotValidId_ThrowsException() throws ObjectNotFoundException {
        // given
        // when
        // then
        assertThrows(ObjectNotFoundException.class, () -> sut.getGame(invalidGameId));
    }

    @Test
    void getGames_WithValidId_ReturnsListOfGames() {
        // given
        List<Game> games = new ArrayList<>(List.of(sharedGame));
        when(gameRepository.findAll()).thenReturn(games);

        // when
        List<Game> result = sut.getGames();

        // then
        verify(gameRepository).findAll();
        assertEquals(games, result);
    }

    @Test
    void deleteGame_WithValidId_DeletesGame() {
        // given
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(sharedGame));

        // when
        sut.deleteGame(validGameId);

        // then
        verify(gameRepository).deleteById(1);
    }

    @Test
    void deleteGame_WithNotValidId_ThrowsException() throws ObjectNotFoundException {
        // given
        // when
        // then
        assertThrows(ObjectNotFoundException.class, () -> sut.deleteGame(invalidGameId));
    }

    @Test
    void deleteAll_DeletesAllGames() {
        // given
        // when
        sut.deleteAll();

        // then
        verify(gameRepository).deleteAll();
    }

    @Test
    void updateGame_WithValidId_UpdatesGame() {
        // given
        Game game = new Game();
        game.setTitle("220");
        Game oldGame = new Game();
        oldGame.setTitle("360");
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(oldGame));

        // when
        sut.updateGame(validGameId, game);

        // then
        verify(gameRepository).save(oldGame);
        assertEquals(game.getTitle(), oldGame.getTitle());
    }

    @Test
    void updateGame_WithNotValidId_ThrowsException() {
        // given
        // when
        // then
        assertThrows(ObjectNotFoundException.class, () -> sut.updateGame(invalidGameId, new Game()));
    }

    @Test
    void patchRating_WithValidRating_ShouldAddRating() {
        // given
        int validPartialRating = 10;
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(new Game()));

        // when
        Float result = sut.patchRating(validGameId, validPartialRating);

        // then
        assertEquals(10, result);
    }

    @Test
    void patchRating_AfterAddingValidRatings_ShouldReturnMeanOfRatings() {
        // given
        int validPartialRatingFirst = 6;
        int validPartialRatingSecond = 7;
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(new Game()));

        // when
        sut.patchRating(validGameId, validPartialRatingFirst);

        // then
        Float result = sut.patchRating(validGameId, validPartialRatingSecond);
        assertEquals(6.5f, result);
    }

    @Test
    void patchRating_AfterAddingValidRatings_ShouldIncreaseReviewCount() {
        // given
        int validPartialRatingFirst = 6;
        int validPartialRatingSecond = 7;
        Game game = new Game();
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(game));

        // when
        sut.patchRating(validGameId, validPartialRatingFirst);
        sut.patchRating(validGameId, validPartialRatingSecond);

        // then
        assertEquals(2, game.getReviewCount());
    }

    @Test
    void patchRating_WithInvalidRating_ShouldNotIncreaseReviewCount() throws OutOfBoundsRatingException {
        // given
        int invalidRatingLowerEnd = 0;
        Game game = new Game();
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(game));

        // when
        try {
            sut.patchRating(validGameId, invalidRatingLowerEnd);
        }catch (OutOfBoundsRatingException ignored){};

        // then
        assertEquals(0, game.getReviewCount());
    }

    @Test
    void patchRating_WithInvalidRating_ThrowsException() {
        // given
        int invalidRatingLowerEnd = 0;
        int invalidRatingHigherEnd = 0;
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(new Game()));

        // when
        // then
        assertThrows(OutOfBoundsRatingException.class, () -> sut.patchRating(validGameId, invalidRatingLowerEnd));
        assertThrows(OutOfBoundsRatingException.class, () -> sut.patchRating(validGameId, invalidRatingHigherEnd));
    }
}