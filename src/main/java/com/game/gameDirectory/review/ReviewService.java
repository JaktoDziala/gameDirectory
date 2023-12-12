package com.game.gameDirectory.review;

import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import com.game.gameDirectory.game.Game;
import com.game.gameDirectory.game.GameService;
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

    void addReview(ReviewDTO review){
        Game game = gameService.getGame(review.gameId());
        gameService.patchRating(game, review.rating());
        reviewRepository.save(
                new Review(game, review.comment(), review.rating()));
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