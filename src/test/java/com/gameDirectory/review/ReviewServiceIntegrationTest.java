package com.gameDirectory.review;

import com.gameDirectory.exceptions.ObjectNotFoundException;
import com.gameDirectory.game.Game;
import com.gameDirectory.game.GameDTO;
import com.gameDirectory.game.GameService;
import com.gameDirectory.game.enums.Genre;
import com.gameDirectory.game.enums.Platform;
import com.gameDirectory.studio.StudioRepository;
import com.gameDirectory.studio.StudioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({ReviewService.class, GameService.class, StudioService.class})
public class ReviewServiceIntegrationTest {

    @Autowired
    private ReviewService sut;
    @Autowired
    private GameService gameService;
    @Autowired
    private StudioService studioService;
    @Autowired
    private StudioRepository studioRepository;

    @AfterEach
    void setAfter(){

    }

    @Test
    void addReview_WithValidDTO_PatchesRating() {
        // given
        // TODO: Add assigning game to studio
        // TODO: INTEGRATION TEST. If game is assigned to studio, is studio game count increased
        Game game = gameService.addGame(new GameDTO("title",
                "description",
                "2000-02-06",
                Platform.PC.toString(),
                Genre.ACTION.toString(),
                null));

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

    @Disabled
    @Test
    void deleteReview_WithValidId_doesNotDeletesGameOfReview(){
        // given
        Game game = new Game();
        gameService.addGame(new GameDTO(null, null, null, null, null, 1));
        ReviewDTO reviewDTO = new ReviewDTO(game.getId(), "xd", 3);
        sut.addReview(reviewDTO);
        // always null
        Set<Review> reviewsBeforeDelete = gameService.getGame(game.getId()).getReviews();
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
