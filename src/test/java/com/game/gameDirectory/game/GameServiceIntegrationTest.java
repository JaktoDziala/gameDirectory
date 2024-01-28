package com.game.gameDirectory.game;

import com.game.gameDirectory.studio.Studio;
import com.game.gameDirectory.studio.StudioDTO;
import com.game.gameDirectory.studio.StudioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({GameService.class, StudioService.class})
public class GameServiceIntegrationTest {

    @Autowired
    private GameService sut;
    @Autowired
    private StudioService studioService;
    private static final String validName = "studio z";
    private static final String validDescription = "description z";

    @Test
    void assignToStudio_withValidIdsAndTransactionalAnnotation_persistsData() {
        // given
        Studio studio = studioService.addStudio(new StudioDTO(
                validName,
                "Transactional studio",
                List.of()
        ));
        Game game = sut.addGame(new GameDTO(
                "Transactional name", "x", "2000-02-06", "PC", "ACTION", null)
        );

        // when
        sut.assignToStudio(1, 1);

        // then
        assertEquals(sut.getGame(1).getStudio(), studio);
        assertEquals(studioService.getStudio(1).getGames().getFirst(), game);
    }

    @Test
    void assignToStudio_withValidIdsAndNotTransactionalAnnotation_doesNotPersistsData() {
        // given
        Studio studio = studioService.addStudio(new StudioDTO(
                validName,
                "Non-transactional studio",
                List.of()
        ));
        Game game = sut.addGame(new GameDTO(
                "Non-transactional name", "x", "2000-02-06", "PC", "ACTION", null)
        );


        List<Game> games = sut.getGames();

        // when
//
//        The behavior you're observing, where the second test creates a game object with ID 2 instead of ID 1,
//        is indicative of a common issue with auto-incremented primary keys in in-memory or embedded
//        databases used in tests.
//
//        In many database systems, sequences or auto-increment counters for generating
//        primary keys are not reset between transactions, even when the transactions are rolled back.
//        This means that while the data is rolled back, the sequence counter is not,
//        leading to the next object being inserted with an incremented ID.
        sut.assignToStudioNoTransaction(2, 2);
        // TODO: Figure out why it's working
        // then
        assertEquals(sut.getGame(1).getStudio(), studio);
        assertEquals(studioService.getStudio(1).getGames().getFirst(), game);
    }
}
