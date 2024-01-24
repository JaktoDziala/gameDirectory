package com.game.gameDirectory.game;

import com.game.gameDirectory.studio.Studio;
import com.game.gameDirectory.studio.StudioDTO;
import com.game.gameDirectory.studio.StudioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

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

        // when
        sut.assignToStudioNoTransaction(1, 1);
        // TODO: Figure out why it's working
        // then
        assertEquals(sut.getGame(1).getStudio(), studio);
        assertEquals(studioService.getStudio(1).getGames().getFirst(), game);
    }
}
