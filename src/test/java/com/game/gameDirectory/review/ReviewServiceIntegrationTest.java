package com.game.gameDirectory.review;

import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import com.game.gameDirectory.game.Game;
import com.game.gameDirectory.game.GameDTO;
import com.game.gameDirectory.game.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ReviewService.class, GameService.class})
public class ReviewServiceIntegrationTest {

    @Autowired
    ReviewService sut;
    @Autowired
    GameService gameService;

    @AfterEach
    void setAfter(){

    }

    @Test
    void addReview_WithValidDTO_PatchesRating() {
        // given
        Game game = new Game();
        gameService.addGame(new GameDTO(null, null, null, null, null, 1));

        final int rating = 8;
        final String comment = "Delete after creation";
        final float previousRating = game.getRating();
        final int previousReviewCount = game.getReviewCount();

        ReviewDTO reviewDTO = new ReviewDTO(
                game.getId(), comment, rating
        );

        // when
        sut.addReview(reviewDTO);

        // then
        assertEquals(previousReviewCount + 1, game.getReviewCount());
        assertEquals((previousRating * previousReviewCount + rating) / (previousReviewCount + 1), game.getRating());
        assertNotEquals(previousRating, game.getRating());
        assertEquals(1, game.getId());
    }


    // TODO: Someday enable
    @Disabled
    @Test
    // @Transactional
    void deleteReview_WithValidId_doesNotDeletesGameOfReview(){
        // given
        Game game = new Game();
        gameService.addGame(new GameDTO(null, null, null, null, null, 1));
        ReviewDTO reviewDTO = new ReviewDTO(game.getId(), "xd", 3);
        sut.addReview(reviewDTO);
        // always null
        List<Review> reviewsBeforeDelete = gameService.getGame(game.getId()).getReviews();
        // when
        sut.deleteReview(sut.getReview(1));
        // then
        Game persistedGame = gameService.getGame(1);
        assertNotNull(persistedGame);
        assertEquals(persistedGame, game);
        assertNotNull(reviewsBeforeDelete);
        assertNull(persistedGame.getReviews());
        assertThrows(ObjectNotFoundException.class, () -> sut.getReview(1));
    }
}
