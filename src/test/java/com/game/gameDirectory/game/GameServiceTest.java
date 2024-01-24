package com.game.gameDirectory.game;

import com.game.gameDirectory.exceptions.InvalidDTOValueException;
import com.game.gameDirectory.exceptions.InvalidDateFormatException;
import com.game.gameDirectory.exceptions.NullObjectException;
import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import com.game.gameDirectory.exceptions.OutOfBoundsRatingException;
import com.game.gameDirectory.game.enums.Genre;
import com.game.gameDirectory.game.enums.Platform;
import com.game.gameDirectory.studio.StudioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    private GameService sut;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private StudioService studioService;
    private static final int validGameId = 1;
    private static final int invalidGameId = -1;
    private Game sharedGame;

    @BeforeEach
    void setUp() {
        sharedGame = new Game();
    }

    @Test
    void addGame_withValidObject_doesNotThrowException() {
        // given
        // when
        // then
        sut.addGame(
                new GameDTO("title",
                        "description",
                        "2000-02-06",
                        Platform.PC.toString(),
                        Genre.ACTION.toString(),
                        1)
        );
    }

    @Test
    void getGame_withValidId_ReturnsGame() {
        // given
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(sharedGame));

        // when
        Game result = sut.getGame(validGameId);

        // then
        verify(gameRepository).findById(1);
        assertEquals(sharedGame, result);
    }

    @Test
    void getGame_withNotValidId_ThrowsException() throws ObjectNotFoundException {
        // given
        // when
        // then
        assertThrows(ObjectNotFoundException.class, () -> sut.getGame(invalidGameId));
    }

    @Test
    void getGames_withValidId_ReturnsListOfGames() {
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
    void deleteGame_withValidId_DeletesGame() {
        // given
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(sharedGame));

        // when
        sut.deleteGame(validGameId);

        // then
        verify(gameRepository).deleteById(1);
    }

    @Test
    void deleteGame_withNullObject_ThrowsException() throws NullObjectException {
        // given
        // when
        // then
        assertThrows(NullObjectException.class, () -> sut.deleteGame(null));
    }

    @Test
    void deleteGame_withNotValidId_ThrowsException() throws ObjectNotFoundException {
        // given
        // when
        // then
        assertThrows(ObjectNotFoundException.class, () -> sut.deleteGame(invalidGameId));
    }

    @Test
    void deleteAll_deletesAllGames() {
        // given
        // when
        sut.deleteAll();

        // then
        verify(gameRepository).deleteAll();
    }

    @Test
    void updateGame_withValidId_UpdatesGame() {
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
    void updateGame_withNotValidId_ThrowsException() {
        // given
        // when
        // then
        assertThrows(ObjectNotFoundException.class, () -> sut.updateGame(invalidGameId, new Game()));
    }

    @Test
    void patchRating_withValidRating_AddsRating() {
        // given
        int validPartialRating = 10;
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(new Game()));

        // when
        Float result = sut.patchRating(validGameId, validPartialRating);

        // then
        assertEquals(10, result);
    }

    @Test
    void patchRating_afterAddingValidRatings_ReturnsMeanOfRatings() {
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
    void patchRating_afterAddingValidRatings_IncreasesReviewCount() {
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
    void patchRating_withNotValidRating_doesNotIncreaseReviewCount() throws OutOfBoundsRatingException {
        // given
        int invalidRatingLowerEnd = 0;
        Game game = new Game();
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(game));

        // when
        try {
            sut.patchRating(validGameId, invalidRatingLowerEnd);
        } catch (OutOfBoundsRatingException ignored) {
        }
        ;

        // then
        assertEquals(0, game.getReviewCount());
    }

    @Test
    void patchRating_withInvalidRating_throwsException() {
        // given
        int invalidRatingLowerEnd = 0;
        int invalidRatingHigherEnd = 0;
        when(gameRepository.findById(validGameId)).thenReturn(Optional.of(new Game()));

        // when
        // then
        assertThrows(OutOfBoundsRatingException.class, () -> sut.patchRating(validGameId, invalidRatingLowerEnd));
        assertThrows(OutOfBoundsRatingException.class, () -> sut.patchRating(validGameId, invalidRatingHigherEnd));
    }

    @Test
    void parseGameReleaseDate_withDateInValidFormat_returnsDate() {
        // given
        final String validDateFormat = "2000-02-06";

        // when
        LocalDate result = sut.parseGameReleaseDate(validDateFormat);

        // then
        assertEquals(validDateFormat, result.toString());
    }

    static Stream<GameDTO> gameDTOStream() {
        String validTitle = "Title";
        String validDescription = "Description";
        String validReleaseDate = "06-02-2000";
        String validPlatform = Platform.PC.toString();
        String validGenre = Genre.ACTION.toString();
        int validId = 1;

        return Stream.of(
                new GameDTO(null, validDescription, validReleaseDate, validPlatform, validGenre, validId),
                new GameDTO(validTitle, null, validReleaseDate, validPlatform, validGenre, validId),
                new GameDTO(validTitle, validDescription, null, validPlatform, validGenre, validId),
                new GameDTO(validTitle, validDescription, validReleaseDate, "EXCEPTION", validGenre, validId),
                new GameDTO(validTitle, validDescription, validReleaseDate, validPlatform, "EXCEPTION", validId)
                );
    }

    @ParameterizedTest
    @MethodSource("gameDTOStream")
    void validateDTO_withInvalidValuesDTO_ThrowsException(GameDTO gameDTO) {
        assertThrows(InvalidDTOValueException.class, () -> sut.validateDTO(gameDTO));
    }

    @CsvSource({
            "06-02-2000",
            "06-2000-02",
            "2000 02 06",
            "20000206",
            "2000-2-6",
            "2000-13-06",
    })
    @ParameterizedTest
    void parseGameReleaseDate_withInvalidDate_throwsException(String date) {
        // given
        // when
        // then
        assertThrows(InvalidDateFormatException.class, () -> sut.parseGameReleaseDate(date));
    }
}