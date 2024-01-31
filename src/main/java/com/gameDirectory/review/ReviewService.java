package com.gameDirectory.review;

import com.gameDirectory.exceptions.InvalidDTOValueException;
import com.gameDirectory.exceptions.ObjectNotFoundException;
import com.gameDirectory.game.Game;
import com.gameDirectory.game.GameService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GameService gameService;

    public ReviewService(ReviewRepository reviewRepository, GameService gameService){
        this.reviewRepository = reviewRepository;
        this.gameService = gameService;
    }

    void addReview(ReviewDTO reviewDTO){
        validateDTO(reviewDTO);

        Game game = gameService.getGame(reviewDTO.gameId());
        gameService.patchRating(game, reviewDTO.rating());
        reviewRepository.save(
                new Review(game, reviewDTO.comment(), reviewDTO.rating()));
    }

    Review getReview(int reviewId){
        return reviewRepository.findById(reviewId).
                orElseThrow(() -> new ObjectNotFoundException("Review with Id " + reviewId + " was not found"));
    }

    List<Review> getReviews(){
        return reviewRepository.findAll();
    }

    void deleteReview(Review review){
        removeGameRating(review.getId());
        reviewRepository.delete(review);
    }
    void deleteReview(int reviewId){
        removeGameRating(reviewId);
        reviewRepository.delete(getReview(reviewId) );
    }

    void deleteAll(){
        reviewRepository.deleteAll();
    }

    void removeGameRating(int reviewId){
        Review review = getReview(reviewId);
        Game game = review.getGame();

        final float oldRating = game.getRating();
        final int reviewCount = game.getReviewCount();
        float newRating = 0;
        if (reviewCount>1)
             newRating = (oldRating * reviewCount - review.getRating()) / (reviewCount - 1);

        // Update the game's rating and review count
        game.setRating(newRating);
        game.setReviewCount(game.getReviewCount() - 1);
    }

    void validateDTO(ReviewDTO reviewDTO) {
        if (reviewDTO.gameId() == null){
            throw new InvalidDTOValueException("gameID of review must not be null!");
        }
        if (StringUtils.isBlank(reviewDTO.comment())){
            throw new InvalidDTOValueException("Comment of review must not be blank!");
        }
        if (reviewDTO.rating() == null){
            throw new InvalidDTOValueException("Rating of review must not be null!");
        }
    }
}