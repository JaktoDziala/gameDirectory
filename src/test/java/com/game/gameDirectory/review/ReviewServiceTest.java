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
import static org.mockito.Mockito.doReturn;
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
    void deleteReview_WithValidReview_DeletesReviewAndRemovesGameRating() {
        // given
        ReviewService spySUT = Mockito.spy(sut);
        Review review = new Review(new Game(), validComment, validRating);
        review.setId(validId); // Set an appropriate review ID

        doReturn(review).when(spySUT).getReview(validId);

        // when
        spySUT.deleteReview(review);

        // then
        verify(reviewRepository).delete(review);
        verify(spySUT).removeGameRating(validId);
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


    // TODO: Finish test cases
    @Test
    void testDeleteReview() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void removeGameRating() {
    }

    @Test
    void getReviews() {
    }
}