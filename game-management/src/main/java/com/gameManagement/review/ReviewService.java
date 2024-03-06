package com.gameManagement.review;

import com.gameManagement.exceptions.ObjectNotFoundException;
import com.gameManagement.game.Game;
import com.gameManagement.game.GameService;
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

    Review addReview(ReviewDTO reviewDTO){
        Game game = gameService.getGame(reviewDTO.gameId());
        gameService.patchRating(game, reviewDTO.rating());
        return reviewRepository.save(
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
}