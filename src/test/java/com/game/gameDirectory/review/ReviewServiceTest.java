package com.game.gameDirectory.review;

import com.game.gameDirectory.game.Game;
import com.game.gameDirectory.game.GameService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @InjectMocks
    private ReviewService sut;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private GameService gameService;

    private final int validGameId = 1;
    private final int rating = 2;
    private final String comment = "2/10";

    @Test
    void addReview_WithValidDto_AddsReviewWithUpdatedRating() {
        // given
        Game game = new Game();
        ReviewDto reviewDto = new ReviewDto(
                validGameId, comment, rating
        );
        when(gameService.getGame(validGameId)).thenReturn(game);

        // when
        sut.addReview(reviewDto);

        // then
        verify(reviewRepository).save(Mockito.any());
        verify(gameService).patchRating(game, reviewDto.getRating());
    }
}