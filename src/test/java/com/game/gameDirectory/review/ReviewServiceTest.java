package com.game.gameDirectory.review;

import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import com.game.gameDirectory.game.Game;
import com.game.gameDirectory.game.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private final int validId = 1;
    private final int invalidId = -1;
    private final int validRating = 2;
    private final String validComment = "2/10";

    @Test
    void addReview_WithValidDTO_AddsReviewWithUpdatedRating() {
        // given
        Game game = new Game();
        ReviewDTO reviewDTO = new ReviewDTO(
                validId, validComment, validRating
        );
        when(gameService.getGame(validId)).thenReturn(game);

        // when
        sut.addReview(reviewDTO);

        // then
        verify(reviewRepository).save(Mockito.any());
        verify(gameService).patchRating(game, reviewDTO.getRating());
    }

    @Test
    void addReview_WithNullIdDTO_ThrowsException() {
        // given
        // when
        // then
        assertThrows(NullPointerException.class,
                () -> sut.addReview(new ReviewDTO(null, validComment, validRating)));
    }

    @Test
    void addReview_WithNullCommentDTO_ThrowsException() {
        // given
        // when
        // then
        assertThrows(NullPointerException.class,
                () -> sut.addReview(new ReviewDTO(validId, null, validRating)));
    }

    @Test
    void addReview_WithEmptyCommentDTO_ThrowsException() {
        // given
        // when
        // then
        assertThrows(NullPointerException.class,
                () -> sut.addReview(new ReviewDTO(validId, "", validRating)));
    }

    @Test
    void addReview_WithNullRatingDTO_ThrowsException() {
        // given
        // when
        // then
        assertThrows(NullPointerException.class,
                () -> sut.addReview(new ReviewDTO(validId, validComment, null)));
    }

    @Test
    void getReview_WithValidId_ReturnsReview() {
        // given
        Review review = new Review();
        when(reviewRepository.findById(validId)).thenReturn(Optional.of(review));

        // when
        Review result =  sut.getReview(validId);

        // then
        verify(reviewRepository).findById(validId);
        assertEquals(review, result);
    }

    @Test
    void getReview_WithNotValidId_ThrowsException() {
        // given
        // when
        // then
        assertThrows(ObjectNotFoundException.class, () -> sut.getReview(invalidId));
    }

    @Test
    void getReviews_GetsAllReviews() {
        // given
        // when
        sut.getReviews();
        // then
        verify(reviewRepository).findAll();
    }

    @Test
    void deleteReview_WithValidReview_DeletesReviewAndRemovesGameRating() {
        // given
        final int firstRating = 9;
        final int reviewCount = 2;
        final float initialRating = (float) (firstRating + validRating) / 2;
        final float expectedRating = firstRating;

        Game game = new Game();
        game.setReviewCount(reviewCount);
        game.setRating(initialRating);
        Review review = new Review(game, validComment, validRating);
        review.setId(validId);
        when(reviewRepository.findById(validId)).thenReturn(Optional.of(review));

        // when
        sut.deleteReview(review);

        // then
        verify(reviewRepository).delete(review);
        assertEquals(expectedRating, game.getRating());
        assertEquals(reviewCount-1, game.getReviewCount());
    }

    @Test
    void deleteReview_WithValidId_DeletesReviewAndRemovesGameRating() {
        // given
        Review review = new Review(new Game(), validComment, validRating);
        review.setId(validId);
        when(reviewRepository.findById(validId)).thenReturn(Optional.of(review));

        // when
        sut.deleteReview(review);

        // then
        verify(reviewRepository).delete(review);
    }

    @Test
    void deleteAll_deletesAllReviews() {
        // given
        // when
        sut.deleteAll();

        // then
        verify(reviewRepository).deleteAll();
    }

    @Test
    void removeGameRating_WithValidId_RemovesGameRating() {
        // given
        final int firstRating = 10;
        final int secondRating = 5;
        final int initialReviewCount = 2;
        final float initialRating = (float) (firstRating + secondRating) / initialReviewCount;

        Game game = new Game();
        game.setReviewCount(initialReviewCount);
        game.setRating(initialRating);

        Review review = new Review(game, validComment, secondRating);

        when(reviewRepository.findById(validId)).thenReturn(Optional.of(review));

        // when
        sut.removeGameRating(validId);

        // then
        assertEquals(firstRating, game.getRating());
        assertEquals(initialReviewCount-1, game.getReviewCount());
    }

    @Test
    void removeGameRating_WithValidIdAndOneRating_SetsGameRatingToZero() {
        // given
        final int firstRating = 10;
        final int initialReviewCount = 1;
        final float initialRating = (float) (firstRating) / initialReviewCount;

        Game game = new Game();
        game.setReviewCount(initialReviewCount);
        game.setRating(initialRating);

        Review review = new Review(game, validComment, firstRating);

        when(reviewRepository.findById(validId)).thenReturn(Optional.of(review));

        // when
        sut.removeGameRating(validId);

        // then
        assertEquals(0, game.getRating());
        assertEquals(initialReviewCount-1, game.getReviewCount());
    }

    @Test
    void removeGameRating_WithValidId_KeepsRatingAsFloat() {
        // given
        final int firstRating = 10;
        final int secondRating = 9;
        final int thirdRating = 8;
        final int initialReviewCount = 3;
        final float initialRating = (float) (firstRating+secondRating+thirdRating) / initialReviewCount;
        final float expectedRating = (float) (firstRating + secondRating) / (initialReviewCount-1);

        Game game = new Game();
        game.setReviewCount(initialReviewCount);
        game.setRating(initialRating);

        Review review = new Review(game, validComment, thirdRating);

        when(reviewRepository.findById(validId)).thenReturn(Optional.of(review));

        // when
        sut.removeGameRating(validId);

        // then
        assertEquals(expectedRating, game.getRating());
        assertEquals(initialReviewCount-1, game.getReviewCount());
    }
}